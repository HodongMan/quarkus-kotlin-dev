package io.hodong.quarkusprac.repository

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Multi; // 
import io.smallrye.mutiny.Uni; // 

import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

import io.hodong.quarkusprac.model.PostModel

@ApplicationScoped
class PostRepository(val client: MySQLPool) {

    fun findAll(): Multi<PostModel> {
        return this.client.query("SELECT * FROM posts")
            .execute()
            .onItem().transformToMulti {
                item -> Multi.createFrom().iterable(item)
            }
            .onItem().transform {
                row -> PostModel(row.getUUID("id"), row.getString("title"), row.getString("description"), row.getLocalDateTime("createdAt"), row.getLocalDateTime("updatedAt"))
            }
    }

    /*
    fun findById(id: UUID): Uni<PostModel> {
        return this.client
                .preparedQuery("SELECT * FROM posts WHERE id=$1")
                .execute(Tuple.of(id))
    }
    */
    
    fun save(post: PostModel): Uni<UUID> {
        return this.client
                .preparedQuery("INSERT INTO posts (title, content) VALUES ($1, $2) RETURNING (id)")
                .execute(Tuple.of(post.title, post.content))

                .onItem().transformToUni {
                    row -> Uni.createFrom().item(row.iterator().next().getUUID("id"))
                }
                //.map {
                //    iter -> iter.iterator().next().getUUID("id")
                //}
    }

    fun update(id: UUID, post: PostModel): Uni<Int> {
        return this.client
                .preparedQuery("UPDATE posts SET title=$1, content=$2 WHERE id=$3")
                .execute(Tuple.of(post.title, post.content, id))
                .map {
                    it -> it.count()
                }
    }
    
    fun delete(id: UUID): Uni<Int> {
        return client.preparedQuery("DELETE FROM posts WHERE id=$1")
                .execute(Tuple.of(id))
                .map {
                    it -> it.count()
                }
    }
}