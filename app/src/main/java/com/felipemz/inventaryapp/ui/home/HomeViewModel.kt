package com.felipemz.inventaryapp.ui.home

import androidx.compose.runtime.mutableStateOf
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.base.BaseViewModel
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.MovementItemEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.enums.MovementType
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

class HomeViewModel : BaseViewModel<HomeState, HomeEvent>() {

    private val _movements = mutableStateOf<List<MovementItemEntity>>(emptyList())

    private val fakeChips = listOf(
        CategoryEntity(name = "Todos"),
        CategoryEntity(name = "Alimentos", color = R.color.lime),
        CategoryEntity(name = "Bebidas", color = R.color.blue),
        CategoryEntity(name = "Cuidado Personal", color = R.color.pink),
        CategoryEntity(name = "Electrónicos", color = R.color.teal),
        CategoryEntity(name = "Hogar", color = R.color.orange),
        CategoryEntity(name = "Limpieza", color = R.color.purple),
        CategoryEntity(name = "Mascotas", color = R.color.green),
        CategoryEntity(name = "Otros", color = R.color.red_dark),
        CategoryEntity(name = "Más")
    )

    private val fakeProducts = listOf(
        ProductEntity(
            name = "Fresa delicia",
            information = "sin información",
            categoryColor = R.color.red_dark,
            quantity = null,
            price = 11000,
            image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
        ),
        ProductEntity(
            name = "Fresa Natural",
            information = "sin información",
            categoryColor = R.color.red_dark,
            quantity = null,
            price = 10000,
            image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
        ),
        ProductEntity(
            name = "Delicia mini",
            information = "sin información",
            categoryColor = R.color.red_dark,
            quantity = null,
            price = 9000,
            image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
        ),
        ProductEntity(
            name = "Coca Cola",
            information = "sin información",
            categoryColor = R.color.blue,
            quantity = 14,
            price = 5000,
            image = ProductTypeImage.EmojiImage("\uD83C\uDF7A")
        ),
        ProductEntity(
            name = "Shampoo",
            information = "Aquí va toda la información del producto / sin información",
            categoryColor = R.color.pink,
            quantity = 3,
            price = 15000,
            image = ProductTypeImage.EmojiImage("\uD83D\uDC4D")
        ),
        ProductEntity(
            name = "Audífonos",
            information = "Aquí va toda la información del producto / sin información",
            categoryColor = R.color.teal,
            quantity = 5,
            price = 50000,
            image = ProductTypeImage.PhatImage("")
        ),
        ProductEntity(
            name = "Cargador",
            information = "Aquí",
            categoryColor = R.color.teal,
            quantity = 6,
            price = 25000,
            image = ProductTypeImage.PhatImage("")
        ),
        ProductEntity(
            name = "Escoba",
            information = "Aquí va toda la información del producto / sin información",
            categoryColor = R.color.orange,
            quantity = 2,
            price = 20000,
            image = ProductTypeImage.LetterImage("Es")
        ),
        ProductEntity(
            name = "Detergente",
            information = "para la losa",
            categoryColor = R.color.purple,
            quantity = 1,
            price = 10000,
            image = ProductTypeImage.LetterImage("De")
        ),
        ProductEntity(
            name = "Comida para perro",
            information = "Aquí va toda la información del producto / sin información",
            categoryColor = R.color.green,
            quantity = 4,
            price = 30000,
            image = ProductTypeImage.EmojiImage("\uD83D\uDC36")
        ),
    )

    private val fakeLabelList = listOf(
        "rappy",
        "domicilios",
        "mesa 1",
        "mesa 2",
        "caja 1",
        "caja 2",
        "fiado",
        "perdida"
    )

    private val fakeMovements = listOf(
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
        ),
    )

    init {
        _movements.value = fakeMovements
    }

    override fun initState() = HomeState(
        categories = fakeChips,
        products = fakeProducts,
        movements = _movements.value,
        movementLabelList = fakeLabelList,
        totalAmount = fakeMovements.sumBy {
            when (it.type) {
                MovementType.MOVEMENT_SALE -> it.amount
                MovementType.MOVEMENT_EXPENSE -> -it.amount
                else -> 0
            }
        }
    )

    override fun intentHandler() {
        executeIntent { event ->
            when (event) {
                is HomeEvent.OnCategorySelected -> {}
                is HomeEvent.OnMovementsInverted -> movementsInverted(event.isInverted)
                is HomeEvent.OnHideLabelPopup -> hideLabelPopup()
                is HomeEvent.OnMovementFilterSelected -> movementFilterSelected(event.filter)
                is HomeEvent.OnLabelSelected -> labelSelected(event.label)
            }
        }
    }

    private fun movementsInverted(isInverted: Boolean) {
        if (state.value.isMovementsInverted != isInverted) {
            updateState {
                it.copy(
                    isMovementsInverted = isInverted,
                    movements = it.movements.reversed()
                )
            }
        }
    }

    private fun labelSelected(label: String) {
        updateState { state ->
            state.copy(
                movementLabelSelected = label,
                isShowLabelPopup = false,
                movements = _movements.value.filter { it.labels.contains(label) }
            )
        }
    }

    private fun movementFilterSelected(filter: MovementsFilterChip) {
        if (filter == MovementsFilterChip.LABEL) {
            updateState { it.copy(isShowLabelPopup = true) }
        } else updateState { it.copy(movementLabelSelected = null) }
        updateState { state ->
            state.copy(
                movementFilterSelected = filter,
                movements = _movements.value.filter {
                    when (filter) {
                        MovementsFilterChip.ALL -> true
                        MovementsFilterChip.INCOME -> it.type == MovementType.MOVEMENT_SALE
                        MovementsFilterChip.EXPENSE -> it.type == MovementType.MOVEMENT_EXPENSE
                        MovementsFilterChip.PENDING -> it.type == MovementType.MOVEMENT_PENDING
                        MovementsFilterChip.LABEL -> it.labels.isEmpty()
                    }
                }
            )
        }
    }

    private fun hideLabelPopup() {
        updateState {
            it.copy(
                isShowLabelPopup = false,
                movementLabelSelected = null
            )
        }
    }
}