package dev.giuliopime.core.db

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import dev.giuliopime.Env
import org.litote.kmongo.KMongo

object MongoClient {
    private val client: MongoClient = KMongo.createClient(Env.mongo_connection_string)
    val database: MongoDatabase = client.getDatabase(Env.mongo_db_name)

    fun close() {
        client.close()
    }
}
