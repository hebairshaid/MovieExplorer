package com.movieexplorer.home_screen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CachedMovieEntity::class], //entities This is a list of all the tables in this database ,why :: class Pass the class itself
    version = 2,  //you first create the database when you add column later it change to 2
    exportSchema = false
    /*exportSchema = false Room can generate a file describing your database schema.
    This is useful in large teams because developers can track database changes over time.
    For a training project, we don't need that file, so we disable it*/
)//Generate a SQLite database using these entities
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
/*
Why is the class abstract?
abstract class MovieDatabase : RoomDatabase()

You never create a MovieDatabase yourself.

Instead, Room generates a concrete implementation behind the scenes.

That's why the class is abstract.
--------------------------------
Why extend RoomDatabase?

RoomDatabase provides all the functionality needed to:

Open the database.
Manage connections.
Execute transactions.
Generate DAO implementations.

Your class inherits all of that functionality.
---------------------------------
Why does Room care about versions?

Imagine a user already has:

Version 1

After updating your app:

Version 2

Room must migrate the old database to the new structure.

Without versioning, users could lose data or the app could crash because the app expects a different table structure than the one stored on the device.
-------------------------------------
Room databases are expensive to create because they:

Open a SQLite connection.
Read the schema.
Prepare SQL statements.

Creating multiple instances wastes memory and can lead to inconsistent data.

Instead, we create one singleton instance for the entire application. Hilt will provide that singleton
*/