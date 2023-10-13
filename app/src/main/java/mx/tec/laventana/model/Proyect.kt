package mx.tec.laventana.model

data class Proyect(
    var id_proyecto: Int,
    var nombre_proyecto: String,
    var categoria_proyecto: String,
    val descripcion_proyecto: String,
    val url_proyecto: String,
    val logo_proyecto: String,
    val imagen_proyecto: String,
    val tipo_proyecto: String,
    val estado: String,
    val latitud: Float,
    val longitud: Float,
    val horario_apertura_proyecto: String,
    val horario_cierre_proyecto: String
)