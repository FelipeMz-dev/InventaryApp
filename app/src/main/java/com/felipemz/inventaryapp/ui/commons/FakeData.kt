package com.felipemz.inventaryapp.ui.commons

import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.CategoryRatingEntity
import com.felipemz.inventaryapp.core.entitys.LabelRatingEntity
import com.felipemz.inventaryapp.core.entitys.MovementItemEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductRatingEntity
import com.felipemz.inventaryapp.core.enums.MovementItemType
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductQuantityChart
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

val fakeChips = listOf(
    CategoryEntity(position = 1, name = "Alimentos", color = R.color.red_dark),
    CategoryEntity(position = 2, name = "Bebidas", color = R.color.blue),
    CategoryEntity(position = 3, name = "Cuidado Personal", color = R.color.pink),
    CategoryEntity(position = 4, name = "Electrónicos", color = R.color.teal),
    CategoryEntity(position = 5, name = "Hogar", color = R.color.orange),
    CategoryEntity(position = 6, name = "Limpieza", color = R.color.purple),
    CategoryEntity(position = 7, name = "Mascotas", color = R.color.green),
    CategoryEntity(position = 8, name = "Otros", color = R.color.lime)
)

val fakeProducts = listOf(
    ProductEntity(
        id = 1,
        name = "Fresa delicia",
        description = "sin información",
        category = fakeChips[0],
        quantityChart = null,
        price = 11000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
    ),
    ProductEntity(
        id = 6,
        name = "Fresa Natural",
        description = "sin información",
        category = fakeChips[0],
        quantityChart = null,
        price = 10000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
    ),
    ProductEntity(
        id = 3,
        name = "Delicia mini",
        description = "sin información",
        category = fakeChips[0],
        quantityChart = null,
        price = 9000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
    ),
    ProductEntity(
        id = 4,
        name = "Coca Cola",
        description = "sin información",
        category = fakeChips[1],
        quantityChart = ProductQuantityChart(QuantityType.UNIT, 5),
        price = 5000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF7A")
    ),
    ProductEntity(
        id = 5,
        name = "Shampoo",
        description = "Aquí va toda la información del producto / sin información",
        category = fakeChips[2],
        quantityChart = ProductQuantityChart(QuantityType.UNIT, 3),
        price = 15000,
        image = ProductTypeImage.EmojiImage("\uD83D\uDC4D")
    ),
    ProductEntity(
        id = 8,
        name = "Audífonos",
        description = "Aquí va toda la información del producto / sin información",
        category = fakeChips[3],
        quantityChart = ProductQuantityChart(QuantityType.UNIT, 1),
        price = 50000,
        image = ProductTypeImage.PhatImage("")
    ),
    ProductEntity(
        id = 9,
        name = "Cargador",
        description = "Aquí",
        category = fakeChips[3],
        quantityChart = ProductQuantityChart(QuantityType.UNIT, 5),
        price = 25000,
        image = ProductTypeImage.PhatImage("")
    ),
    ProductEntity(
        id = 7,
        name = "Escoba",
        description = "Aquí va toda la información del producto / sin información",
        category = fakeChips[4],
        quantityChart = ProductQuantityChart(QuantityType.UNIT, 20),
        price = 20000,
        image = ProductTypeImage.LetterImage("Es")
    ),
    ProductEntity(
        id = 2,
        name = "Detergente",
        description = "para la losa",
        category = fakeChips[5],
        quantityChart = ProductQuantityChart(QuantityType.UNIT, 13),
        price = 10000,
        image = ProductTypeImage.LetterImage("De")
    ),
    ProductEntity(
        id = 10,
        name = "Comida para perro",
        description = "Aquí va toda la información del producto / sin información",
        category = fakeChips[6],
        quantityChart = ProductQuantityChart(QuantityType.GRAM, 10),
        price = 30000,
        image = ProductTypeImage.EmojiImage("\uD83D\uDC36")
    ),
)

val fakeLabelList = listOf(
    "rappy",
    "domicilios",
    "mesa 1",
    "mesa 2",
    "caja 1",
    "caja 2",
    "fiado",
    "perdida"
)

val fakeMovements = listOf(
    MovementItemEntity(
        type = MovementItemType.MOVEMENT_SALE,
        number = 1,
        date = "12/12/2021",
        time = "12:00 pm",
        amount = 10000,
    ),
    MovementItemEntity(
        type = MovementItemType.MOVEMENT_SALE,
        number = 2,
        date = "12/12/2021",
        time = "1:40 pm",
        amount = 10500,
        labels = listOf("rappy", "domicilios", "mesa 1", "mesa 2", "caja 1", "caja 2")
    ),
    MovementItemEntity(
        type = MovementItemType.MOVEMENT_SALE,
        number = 3,
        date = "12/12/2021",
        time = "2:00 pm",
        amount = 11000,
        labels = listOf("fiado"),
    ),
    MovementItemEntity(
        type = MovementItemType.MOVEMENT_PENDING,
        date = "12/12/2021",
        time = "4:00 pm",
        amount = 8000,
        labels = listOf("fiado", "caja 1", "caja 2")
    ),
    MovementItemEntity(
        type = MovementItemType.MOVEMENT_EXPENSE,
        number = 1,
        date = "12/12/2021",
        time = "4:20 pm",
        amount = 2050,
        labels = listOf("caja 1"),
    ),
    MovementItemEntity(
        type = MovementItemType.MOVEMENT_SALE,
        number = 4,
        date = "12/12/2021",
        time = "5:30 pm",
        amount = 2000,
    ),
    MovementItemEntity(
        type = MovementItemType.MOVEMENT_EXPENSE,
        number = 2,
        date = "12/12/2021",
        time = "6:00 pm",
        labels = listOf("rappy", "caja 1"),
        amount = 500,
    ),
    MovementItemEntity(
        type = MovementItemType.MOVEMENT_SALE,
        number = 3,
        date = "12/12/2021",
        time = "6:40 pm",
        amount = 1500,
        labels = listOf("rappy", "domicilios", "caja 1", "caja 2", "perdida")
    )
)

val fakeIntervals = listOf(
    Pair("de las 00:00 a las 06:00", 0),
    Pair("de las 06:00 a las 12:00", 100),
    Pair("de las 12:00 a las 18:00", 200),
    Pair("de las 18:00 a las 24:00", 300)
)

val fakeLabelsRating = listOf(
    LabelRatingEntity(
        label = "Promoción Promoción Promoción Promoción",
        rating = 100,
        totalValue = 300000
    ),
    LabelRatingEntity(
        label = "Domicilio",
        rating = 98,
        totalValue = 200000
    ),
    LabelRatingEntity(
        label = "Oferta",
        rating = 90,
        totalValue = 100000
    ),
    LabelRatingEntity(
        label = "Descuento",
        rating = 87,
        totalValue = 10000
    ),
    LabelRatingEntity(
        label = "Gratis",
        rating = 60,
        totalValue = 1000
    )
)

val fakeCategoriesRating = listOf(
    CategoryRatingEntity(
        category = fakeChips[0],
        rating = 100,
        totalValue = 10000
    ),
    CategoryRatingEntity(
        category = fakeChips[1],
        rating = 98,
        totalValue = 14000
    ),
    CategoryRatingEntity(
        category = fakeChips[2],
        rating = 90,
        totalValue = 20000
    ),
    CategoryRatingEntity(
        category = fakeChips[3],
        rating = 87,
        totalValue = 20500
    ),
    CategoryRatingEntity(
        category = fakeChips[4],
        rating = 60,
        totalValue = 1200
    )
)

val fakeProductsRating = listOf(
    ProductRatingEntity(
        product = fakeProducts[0],
        rating = 100,
        totalValue = 10000
    ),
    ProductRatingEntity(
        product = fakeProducts[3],
        rating = 98,
        totalValue = 14000
    ),
    ProductRatingEntity(
        product = fakeProducts[4],
        rating = 90,
        totalValue = 20000
    ),
    ProductRatingEntity(
        product = fakeProducts[5],
        rating = 87,
        totalValue = 20500
    ),
    ProductRatingEntity(
        product = fakeProducts[6],
        rating = 60,
        totalValue = 1200
    )
)
