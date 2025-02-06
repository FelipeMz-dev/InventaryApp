package com.felipemz.inventaryapp.core.enums

enum class QuantityType(
    val text: String,
    val initial: String
) {
    UNIT("Unidad", "u"),
    OUNCE("Onza", "Oz"),
    GRAM("Gramo", "g"),
    KILOGRAM("Kilogramo", "Kg"),
    MILLIGRAM("Miligramo", "mg"),
    LIBRA("Libra", "Lb"),
    LITER("Litro", "L"),
    MILLILITER("Mililitro", "mL"),
    METER("Metro", "m"),
    CENTIMETER("Centímetro", "cm"),
    MILLIMETER("Milímetro", "mm"), ;
}