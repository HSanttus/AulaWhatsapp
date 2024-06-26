package com.example.aulawhatsapp

import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.aulawhatsapp.databinding.ActivityPerfilBinding
import com.example.aulawhatsapp.utils.exibirMensagens

class PerfilActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPerfilBinding.inflate(layoutInflater)
    }

    private val gerenciadorGaleria = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            binding.imagePerfil.setImageURI(uri)
        } else {
            exibirMensagens("Nenhuma mensagem selecionada")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarToolbar()
        inicializarEventosCliques()
    }

    private fun inicializarEventosCliques() {
        binding.fabSelecionar.setOnClickListener {
            gerenciadorGaleria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun inicializarToolbar() {
        val toolbar = binding.includeToolbarPerfil.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Editar Perfil"
            setDisplayHomeAsUpEnabled(true)//por uma seta para voltar a ativity anterior
        }
    }
}