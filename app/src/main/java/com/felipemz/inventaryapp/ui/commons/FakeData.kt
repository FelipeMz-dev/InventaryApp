package com.felipemz.inventaryapp.ui.commons

import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.MovementItemEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.enums.MovementType
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
        categoryColor = R.color.red_dark,
        quantity = null,
        price = 11000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
    ),
    ProductEntity(
        id = 6,
        name = "Fresa Natural",
        description = "sin información",
        categoryColor = R.color.red_dark,
        quantity = null,
        price = 10000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
    ),
    ProductEntity(
        id = 3,
        name = "Delicia mini",
        description = "sin información",
        categoryColor = R.color.red_dark,
        quantity = null,
        price = 9000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
    ),
    ProductEntity(
        id = 4,
        name = "Coca Cola",
        description = "sin información",
        categoryColor = R.color.blue,
        quantity = 14,
        price = 5000,
        image = ProductTypeImage.EmojiImage("\uD83C\uDF7A")
    ),
    ProductEntity(
        id = 5,
        name = "Shampoo",
        description = "Aquí va toda la información del producto / sin información",
        categoryColor = R.color.pink,
        quantity = 3,
        price = 15000,
        image = ProductTypeImage.EmojiImage("\uD83D\uDC4D")
    ),
    ProductEntity(
        id = 8,
        name = "Audífonos",
        description = "Aquí va toda la información del producto / sin información",
        categoryColor = R.color.teal,
        quantity = 5,
        price = 50000,
        image = ProductTypeImage.PhatImage("")
    ),
    ProductEntity(
        id = 9,
        name = "Cargador",
        description = "Aquí",
        categoryColor = R.color.teal,
        quantity = 6,
        price = 25000,
        image = ProductTypeImage.PhatImage("")
    ),
    ProductEntity(
        id = 7,
        name = "Escoba",
        description = "Aquí va toda la información del producto / sin información",
        categoryColor = R.color.orange,
        quantity = 2,
        price = 20000,
        image = ProductTypeImage.LetterImage("Es")
    ),
    ProductEntity(
        id = 2,
        name = "Detergente",
        description = "para la losa",
        categoryColor = R.color.purple,
        quantity = 1,
        price = 10000,
        image = ProductTypeImage.LetterImage("De")
    ),
    ProductEntity(
        id = 10,
        name = "Comida para perro",
        description = "Aquí va toda la información del producto / sin información",
        categoryColor = R.color.green,
        quantity = 4,
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
        type = MovementType.MOVEMENT_SALE,
        number = 1,
        date = "12/12/2021",
        time = "12:00 pm",
        amount = 10000,
    ),
    MovementItemEntity(
        type = MovementType.MOVEMENT_SALE,
        number = 2,
        date = "12/12/2021",
        time = "1:40 pm",
        amount = 10500,
        labels = listOf("rappy", "domicilios", "mesa 1", "mesa 2", "caja 1", "caja 2")
    ),
    MovementItemEntity(
        type = MovementType.MOVEMENT_SALE,
        number = 3,
        date = "12/12/2021",
        time = "2:00 pm",
        amount = 11000,
        labels = listOf("fiado"),
    ),
    MovementItemEntity(
        type = MovementType.MOVEMENT_PENDING,
        date = "12/12/2021",
        time = "4:00 pm",
        amount = 8000,
        labels = listOf("fiado", "caja 1", "caja 2")
    ),
    MovementItemEntity(
        type = MovementType.MOVEMENT_EXPENSE,
        number = 1,
        date = "12/12/2021",
        time = "4:20 pm",
        amount = 2050,
        labels = listOf("caja 1"),
    ),
    MovementItemEntity(
        type = MovementType.MOVEMENT_SALE,
        number = 4,
        date = "12/12/2021",
        time = "5:30 pm",
        amount = 2000,
    ),
    MovementItemEntity(
        type = MovementType.MOVEMENT_EXPENSE,
        number = 2,
        date = "12/12/2021",
        time = "6:00 pm",
        labels = listOf("rappy", "caja 1"),
        amount = 500,
    ),
    MovementItemEntity(
        type = MovementType.MOVEMENT_SALE,
        number = 3,
        date = "12/12/2021",
        time = "6:40 pm",
        amount = 1500,
        labels = listOf("rappy", "domicilios", "caja 1", "caja 2", "perdida")
    )
)