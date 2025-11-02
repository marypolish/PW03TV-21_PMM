package com.example.myapplication

import kotlin.math.pow
import kotlin.math.sqrt

// --- Константи й структури ---
data class LoadData(
    val shlifN: Double = 4.00,
    val poliN: Double = 1.00,
    val tsirkylN: Double = 1.00,

    val Nn: Double = 0.92,     // ККД (η)
    val cosphi: Double = 0.9,  // cos(φ)
    val Un: Double = 0.38,     // Напруга (кВ)

    // Фіксовані суми для ШР1
    val allNPn: Double = 456.00,
    val allNPn2: Double = 14792.00,
    val PnKvN: Double = 95.16,
    val PnKvNTgphi: Double = 107.302,

    // Фіксовані суми для Цеху
    val allNPnTseh: Double = 2330.00,
    val allNPn2Tseh: Double = 96388.00,
    val PnKvNTseh: Double = 752.00,
    val PnKvNTgphiTseh: Double = 657.00
)

data class CalculationResults(
    val ipShlif: Double,
    val ipPoli: Double,
    val ipTsirkyl: Double,

    val allKv: Double,
    val ne: Double,

    val ppShR1: Double,
    val qpShR1: Double,
    val spShR1: Double,
    val ipShR1: Double,

    val allKvTseh: Double,
    val neTseh: Double,
    val ppTseh: Double,
    val qpTseh: Double,
    val spTseh: Double,
    val ipTseh: Double
)

fun calculateLoad(Pn: Double, Kv: Double, tgPhi: Double): CalculationResults {
    val data = LoadData()
    val Kp = 1.25
    val KpTseh = 0.7
    val SQRT_3 = sqrt(3.0)

    // 1) окремі верстати: I = (n * Pn) / (√3 * Un * cosφ * η)
    val ipShlif = (data.shlifN * Pn) / (SQRT_3 * data.Un * data.cosphi * data.Nn)
    val ipPoli = (data.poliN * Pn) / (SQRT_3 * data.Un * data.cosphi * data.Nn)
    val ipTsirkyl = (data.tsirkylN * Pn) / (SQRT_3 * data.Un * data.cosphi * data.Nn)

    // 2) ШР1 (використовуються фіксовані суми)
    val allKv = data.PnKvN / data.allNPn
    val ne = data.allNPn.pow(2) / data.allNPn2

    val ppShR1 = Kp * data.PnKvN                     // активна потужність (кВт)
    val qpShR1 = 1.0 * data.PnKvNTgphi               // реактивна (квар)
    val spShR1 = sqrt(ppShR1.pow(2) + qpShR1.pow(2)) // повна (кВА)

    // *** ЗМІНА: груповий струм ШР1 за вашим очікуванням (без √3, cosφ та η) ***
    val ipShR1 = ppShR1 / data.Un

    // 3) Цех
    val allKvTseh = data.PnKvNTseh / data.allNPnTseh
    val neTseh = data.allNPnTseh.pow(2) / data.allNPn2Tseh

    val ppTseh = KpTseh * data.PnKvNTseh
    val qpTseh = KpTseh * data.PnKvNTgphiTseh
    val spTseh = sqrt(ppTseh.pow(2) + qpTseh.pow(2))

    // *** ЗМІНА: груповий струм Цеху також як у вашому прикладі ***
    val ipTseh = ppTseh / data.Un

    return CalculationResults(
        ipShlif = ipShlif,
        ipPoli = ipPoli,
        ipTsirkyl = ipTsirkyl,
        allKv = allKv,
        ne = ne,
        ppShR1 = ppShR1,
        qpShR1 = qpShR1,
        spShR1 = spShR1,
        ipShR1 = ipShR1,
        allKvTseh = allKvTseh,
        neTseh = neTseh,
        ppTseh = ppTseh,
        qpTseh = qpTseh,
        spTseh = spTseh,
        ipTseh = ipTseh
    )
}
