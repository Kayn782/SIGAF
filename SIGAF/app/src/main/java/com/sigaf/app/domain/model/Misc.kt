package com.sigaf.app.domain.model

/** Consejo de seguridad que se muestra en el inicio. */
data class SecurityTip(
    val title: String,
    val message: String
)

/** Elemento bloqueado (remitente o enlace) en el Centro de Seguridad. */
data class BlockedItem(
    val id: String,
    val value: String,
    val type: BlockType,
    val addedAt: String
)
