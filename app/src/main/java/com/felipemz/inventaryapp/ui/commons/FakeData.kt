package com.felipemz.inventaryapp.ui.commons

import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.MovementItemType
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.CategoryRatingModel
import com.felipemz.inventaryapp.domain.model.LabelRatingModel
import com.felipemz.inventaryapp.domain.model.MovementModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductQuantityModel
import com.felipemz.inventaryapp.domain.model.ProductRatingModel
import com.felipemz.inventaryapp.domain.model.ProductTypeImage

val fakeChips = listOf(
    CategoryModel(id = 1, position = 1, name = "Alimentos", color = R.color.red_dark),
    CategoryModel(id = 2, position = 2, name = "Bebidas", color = R.color.blue),
    CategoryModel(id = 3, position = 3, name = "Cuidado Personal", color = R.color.pink),
    CategoryModel(id = 4, position = 4, name = "Electrónicos", color = R.color.teal),
    CategoryModel(id = 5, position = 5, name = "Hogar", color = R.color.orange),
    CategoryModel(id = 6, position = 6, name = "Limpieza", color = R.color.purple),
    CategoryModel(id = 7, position = 7, name = "Mascotas", color = R.color.green),
    CategoryModel(id = 8, position = 8, name = "Otros", color = R.color.lime)
)

val fakeProducts = listOf(
    ProductModel(
        id = 1,
        name = "Fresa delicia",
        description = "sin información",
        category = fakeChips[0],
        quantityModel = null,
        price = 11000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
    ),
    ProductModel(
        id = 6,
        name = "Fresa Natural",
        description = "sin información",
        category = fakeChips[0],
        quantityModel = null,
        price = 10000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
    ),
    ProductModel(
        id = 3,
        name = "Delicia mini",
        description = "sin información",
        category = fakeChips[0],
        quantityModel = null,
        price = 9000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
    ),
    ProductModel(
        id = 4,
        name = "Coca Cola",
        description = "sin información",
        category = fakeChips[1],
        quantityModel = ProductQuantityModel(QuantityType.UNIT, 5),
        price = 5000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF7A")
    ),
    ProductModel(
        id = 5,
        name = "Shampoo",
        description = "Aquí va toda la información del producto / sin información",
        category = fakeChips[2],
        quantityModel = ProductQuantityModel(QuantityType.UNIT, 3),
        price = 15000,
        image = ProductTypeImage.EmojiImage("\uD83D\uDC4D")
    ),
    ProductModel(
        id = 8,
        name = "Audífonos",
        description = "Aquí va toda la información del producto / sin información",
        category = fakeChips[3],
        quantityModel = ProductQuantityModel(QuantityType.UNIT, 1),
        price = 50000,
        image = ProductTypeImage.PhatImage("")
    ),
    ProductModel(
        id = 9,
        name = "Cargador",
        description = "Aquí",
        category = fakeChips[3],
        quantityModel = ProductQuantityModel(QuantityType.UNIT, 5),
        price = 25000,
        image = ProductTypeImage.PhatImage("")
    ),
    ProductModel(
        id = 7,
        name = "Escoba",
        description = "Aquí va toda la información del producto / sin información",
        category = fakeChips[4],
        quantityModel = ProductQuantityModel(QuantityType.UNIT, 20),
        price = 20000,
        image = ProductTypeImage.LetterImage("Es")
    ),
    ProductModel(
        id = 2,
        name = "Detergente",
        description = "para la losa",
        category = fakeChips[5],
        quantityModel = ProductQuantityModel(QuantityType.UNIT, 13),
        price = 10000,
        image = ProductTypeImage.LetterImage("De")
    ),
    ProductModel(
        id = 10,
        name = "Comida para perro",
        description = "Aquí va toda la información del producto / sin información",
        category = fakeChips[6],
        quantityModel = ProductQuantityModel(QuantityType.GRAM, 10),
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
    MovementModel(
        id = 1,
        type = MovementItemType.MOVEMENT_SALE,
        number = 1,
        date = "12/12/2021",
        time = "12:00 pm",
        total = 10000,
    ),
    MovementModel(
        id = 2,
        type = MovementItemType.MOVEMENT_SALE,
        number = 2,
        date = "12/12/2021",
        time = "1:40 pm",
        total = 10500,
        labels = listOf("rappy", "domicilios", "mesa 1", "mesa 2", "caja 1", "caja 2")
    ),
    MovementModel(
        id = 3,
        type = MovementItemType.MOVEMENT_SALE,
        number = 3,
        date = "12/12/2021",
        time = "2:00 pm",
        total = 11000,
        labels = listOf("fiado"),
    ),
    MovementModel(
        id = 4,
        type = MovementItemType.MOVEMENT_PENDING,
        date = "12/12/2021",
        time = "4:00 pm",
        total = 8000,
        labels = listOf("fiado", "caja 1", "caja 2")
    ),
    MovementModel(
        id = 5,
        type = MovementItemType.MOVEMENT_EXPENSE,
        number = 1,
        date = "12/12/2021",
        time = "4:20 pm",
        total = 2050,
        labels = listOf("caja 1"),
    ),
    MovementModel(
        id = 6,
        type = MovementItemType.MOVEMENT_SALE,
        number = 4,
        date = "12/12/2021",
        time = "5:30 pm",
        total = 2000,
    ),
    MovementModel(
        id = 7,
        type = MovementItemType.MOVEMENT_EXPENSE,
        number = 2,
        date = "12/12/2021",
        time = "6:00 pm",
        labels = listOf("rappy", "caja 1"),
        total = 500,
    ),
    MovementModel(
        id = 8,
        type = MovementItemType.MOVEMENT_SALE,
        number = 3,
        date = "12/12/2021",
        time = "6:40 pm",
        total = 1500,
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
    LabelRatingModel(
        label = "Promoción Promoción Promoción Promoción",
        rating = 100,
        totalValue = 300000
    ),
    LabelRatingModel(
        label = "Domicilio",
        rating = 98,
        totalValue = 200000
    ),
    LabelRatingModel(
        label = "Oferta",
        rating = 90,
        totalValue = 100000
    ),
    LabelRatingModel(
        label = "Descuento",
        rating = 87,
        totalValue = 10000
    ),
    LabelRatingModel(
        label = "Gratis",
        rating = 60,
        totalValue = 1000
    )
)

val fakeCategoriesRating = listOf(
    CategoryRatingModel(
        category = fakeChips[0],
        rating = 100,
        totalValue = 10000
    ),
    CategoryRatingModel(
        category = fakeChips[1],
        rating = 98,
        totalValue = 14000
    ),
    CategoryRatingModel(
        category = fakeChips[2],
        rating = 90,
        totalValue = 20000
    ),
    CategoryRatingModel(
        category = fakeChips[3],
        rating = 87,
        totalValue = 20500
    ),
    CategoryRatingModel(
        category = fakeChips[4],
        rating = 60,
        totalValue = 1200
    )
)

val fakeProductsRating = listOf(
    ProductRatingModel(
        product = fakeProducts[0],
        rating = 100,
        totalValue = 10000
    ),
    ProductRatingModel(
        product = fakeProducts[3],
        rating = 98,
        totalValue = 14000
    ),
    ProductRatingModel(
        product = fakeProducts[4],
        rating = 90,
        totalValue = 20000
    ),
    ProductRatingModel(
        product = fakeProducts[5],
        rating = 87,
        totalValue = 20500
    ),
    ProductRatingModel(
        product = fakeProducts[6],
        rating = 60,
        totalValue = 1200
    )
)
