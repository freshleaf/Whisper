package io.buzznerd.varys.whisper.component;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Migration for realm database
 * <br><br>
 * Created on 16/8/29.
 *
 * @author Xingye
 * @since 1.0.0
 */
public class WhisperRealmMigration implements RealmMigration {

    /**
     * current schema version
     */
    public static final int CURRENT_REALM_SCHEMA_VERSION = 0;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();

        // example:
        /*

        // Add a new class
        if (oldVersion == 0) {
            schema.create("NewObject")
                    .addField("field1", String.class)
                    .addField("field2", Int.class);
            oldVersion++;
        }

        // Add a primary key + object references
        if (oldVersion == 1) {
            schema.get("Person")
                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                    .addRealmObjectField("favoriteDog", schema.get("Dog"))
                    .addRealmListField("dogs", schema.get("Dog"));
            oldVersion++;
        }

        // see more: https://realm.io/docs/java/latest/#migrations
        */

    }
}
