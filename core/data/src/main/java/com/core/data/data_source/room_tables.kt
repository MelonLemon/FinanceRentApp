package com.core.data.data_source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "blocks")
data class Blocks(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "block_id", index = true) val blockId: Int?,
    @ColumnInfo(name = "block_category", index = true) val blockCategory: String,
    @ColumnInfo(name = "name", index = true) val name: String
)

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Blocks::class,
            parentColumns = ["block_id"],
            childColumns = ["block_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Categories::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Transactions(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "transaction_id", index = true) val transactionId: Int?,
    @ColumnInfo(name = "block_id", index = true) val blockId: Int,
    @ColumnInfo(name = "category_id", index = true) val categoryId: Int,
    val amount: Int,
    val currency_name: String,
    val year: Int,
    val month: Int,
    @ColumnInfo(name = "current_date") val currentDate: Long,
    val comment: String
)

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = Blocks::class,
            parentColumns = ["block_id"],
            childColumns = ["block_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Categories(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "category_id", index = true) val categoryId: Int?,
    @ColumnInfo(name = "block_id", index = true) val blockId: Int,
    @ColumnInfo(name = "standard_category_id") val standardCategoryId: Int,
    @ColumnInfo(name = "is_income") val isIncome: Boolean,
    @ColumnInfo(name = "name") val name: String
)

@Entity(tableName = "rents",
    foreignKeys = [
        ForeignKey(
            entity = Blocks::class,
            parentColumns = ["block_id"],
            childColumns = ["block_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Rents(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rent_id", index = true) val rentId: Int?,
    @ColumnInfo(name = "block_id") val blockId: Int,
    val name: String,
    val phone: String,
    val comment: String,
    @ColumnInfo(name = "start_date") val startDate: Long?,
    @ColumnInfo(name = "end_date") val endDate: Long?,
    @ColumnInfo(name = "for_night") val forNight: Int,
    @ColumnInfo(name = "for_all_nights") val forAllNights: Int,
    @ColumnInfo(name = "nights") val nights: Int,
    @ColumnInfo(name = "is_paid") val isPaid: Boolean
)

@Entity(tableName = "rents_track",
    foreignKeys = [
        ForeignKey(
            entity = Rents::class,
            parentColumns = ["rent_id"],
            childColumns = ["rent_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class RentsTrack(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "track_id", index = true) val trackId: Int?,
    @ColumnInfo(name = "rent_id") val rentId: Int,
    val year: Int,
    val month: Int,
    @ColumnInfo(name = "nights") val nights: Int,
    @ColumnInfo(name = "list_full_days") val listDates: List<Long>?,
    @ColumnInfo(name = "list_half_days") val listHalfDates: List<Long>?,
    val amount: Int,
    @ColumnInfo(name = "is_paid") val isPaid: Boolean,
    @ColumnInfo(name = "transaction_id") val transaction_id: Int?,
)

data class IncomeExpenses(
    val income: Int,
    val expenses: Int
)

