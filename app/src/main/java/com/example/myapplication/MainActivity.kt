package com.example.myapplication

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.myapplication.databinding.ActivityMainBinding
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // підписки
        binding.inputPn.setText(viewModel.pnInput.value)
        binding.inputKv.setText(viewModel.kvInput.value)
        binding.inputTgphi.setText(viewModel.tgphiInput.value)

        binding.inputPn.doAfterTextChangedSafe { viewModel.updatePnInput(it) }
        binding.inputKv.doAfterTextChangedSafe { viewModel.updateKvInput(it) }
        binding.inputTgphi.doAfterTextChangedSafe { viewModel.updateTgPhiInput(it) }

        binding.btnCalculate.setOnClickListener { viewModel.calculate() }

        viewModel.results.observe(this) { res ->
            if (res != null) showResults(res) else clearResults()
        }
        viewModel.error.observe(this) { err ->
            binding.inputPnLayout.error = err
        }
    }

    private fun showResults(r: CalculationResults) {
        fun Double.f(digits: Int) = "%.${digits}f".format(this)

        binding.textIpShlif.text = "Ip (Шліфувальний): ${r.ipShlif.f(3)} A"
        binding.textIpPoli.text = "Ip (Полірувальний): ${r.ipPoli.f(3)} A"
        binding.textIpTsirkyl.text = "Ip (Циркулярна пила): ${r.ipTsirkyl.f(3)} A"

        binding.textKvShrif.text = "Груповий Kv: ${r.allKv.f(3)}"
        binding.textNeShrif.text = "Ефективна кількість ЕП (Ne): ${r.ne.f(3)}"
        binding.textPpShrif.text = "Pp (Активна потужність): ${r.ppShR1.f(3)} кВт"
        binding.textQpShrif.text = "Qp (Реактивне навантаження): ${r.qpShR1.f(3)} квар"
        binding.textSpShrif.text = "Sp (Повна потужність): ${r.spShR1.f(3)} кВ*А"
        binding.textIpShrif.text = "Ip (Груповий струм): ${r.ipShR1.f(3)} A"

        binding.textKvTseh.text = "Груповий Kv цеху: ${r.allKvTseh.f(3)}"
        binding.textNeTseh.text = "Ефективна кількість ЕП цеху (Ne): ${r.neTseh.f(3)}"
        binding.textPpTseh.text = "Pp (Активна потужність): ${r.ppTseh.f(3)} кВт"
        binding.textQpTseh.text = "Qp (Реактивне навантаження): ${r.qpTseh.f(3)} квар"
        binding.textSpTseh.text = "Sp (Повна потужність): ${r.spTseh.f(3)} кВ*А"
        binding.textIpTseh.text = "Ip (Груповий струм): ${r.ipTseh.f(3)} А"
    }

    private fun clearResults() {
        val d = "..."
        binding.textIpShlif.text = d
        binding.textIpPoli.text = d
        binding.textIpTsirkyl.text = d

        binding.textKvShrif.text = d
        binding.textNeShrif.text = d
        binding.textPpShrif.text = d
        binding.textQpShrif.text = d
        binding.textSpShrif.text = d
        binding.textIpShrif.text = d

        binding.textKvTseh.text = d
        binding.textNeTseh.text = d
        binding.textPpTseh.text = d
        binding.textQpTseh.text = d
        binding.textSpTseh.text = d
        binding.textIpTseh.text = d
    }
}

/** Маленький extension щоб не імпортувати doAfterTextChanged з core.ktx вручну */
private fun com.google.android.material.textfield.TextInputEditText.doAfterTextChangedSafe(action: (String) -> Unit) {
    this.addTextChangedListener(afterTextChanged = { s -> action.invoke(s?.toString() ?: "") })
}
