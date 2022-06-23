package com.example.swapi.data

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import io.realm.RealmSchema
import io.realm.annotations.Ignore

class FilmRealmMigration:RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        migrate1to2(realm.schema)
    }


    private fun migrate1to2(schema: RealmSchema?){
        schema!!.create("FilmDb")
            .addField("id",Int::class.java,FieldAttribute.PRIMARY_KEY)
            .addField("title", String::class.java,FieldAttribute.REQUIRED)
            .addField("opening_crawl", String::class.java,FieldAttribute.REQUIRED)
    }
}