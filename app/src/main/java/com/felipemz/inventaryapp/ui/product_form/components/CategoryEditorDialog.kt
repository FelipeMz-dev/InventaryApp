package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.enums.CategoryColor
import com.felipemz.inventaryapp.core.extensions.onColor
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import com.felipemz.inventaryapp.ui.product_form.field.NameField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditorDialog(
    category: CategoryModel,
    enableColors: List<CategoryColor>,
    onDismiss: () -> Unit,
    onInsertOrCreate: (CategoryModel) -> Unit
) {

    val textSave = if (category.id == 0) "Crear categoría" else "Editar categoría"
    val stateCarousel = rememberCarouselState { enableColors.size }
    var newCategory by remember { mutableStateOf(category) }
    val enableCreation by remember {
        derivedStateOf { (newCategory.color != 0 && newCategory.name.isNotEmpty()) }
    }

    BasicAlertDialog(
        modifier = Modifier,
        onDismissRequest = { onDismiss() }
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Categoría",
                style = MaterialTheme.typography.titleLarge,
            )

            NameField(
                modifier = Modifier.fillMaxWidth(),
                name = newCategory.name,
            ) { newCategory = newCategory.copy(name = it) }

            HorizontalUncontainedCarousel(
                modifier = Modifier.fillMaxWidth(),
                state = stateCarousel,
                itemSpacing = 8.dp,
                itemWidth = 40.dp,
            ) { index ->
                val color = colorResource(enableColors[index].colorId)

                Box {
                    Spacer(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(color)
                            .clickable { newCategory = newCategory.copy(color = enableColors[index].colorId) }
                            .size(40.dp)
                    )

                    if (newCategory.color == enableColors[index].colorId) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                            tint = color.onColor()
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                TextButtonUnderline(
                    text = "Cancelar"
                ) { onDismiss() }

                TextButtonUnderline(
                    text = textSave,
                    isEnabled = enableCreation
                ) { onInsertOrCreate(newCategory) }

            }
        }
    }
}