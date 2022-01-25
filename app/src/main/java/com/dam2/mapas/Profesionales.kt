package com.dam2.mapas

data class Caracteristica(
    var nombre: String? = null,
    var lg: Double? = null,
    var lt: Double? = null) {}

data class Profesionales(var id: String? = null){
    var caracteristica?: Cararacteristica = null
}

