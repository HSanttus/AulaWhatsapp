package com.example.aulawhatsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aulawhatsapp.databinding.ActivityCadastroBinding
import com.example.aulawhatsapp.model.Usuario
import com.example.aulawhatsapp.utils.exibirMensagens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private lateinit var nome: String
    private lateinit var email: String
    private lateinit var senha: String

    //Firebase
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializarToolbar()
        inicalizarEventosClique()
    }

    private fun inicalizarEventosClique() {
        binding.btnCadastrar.setOnClickListener {
            if (validarCampos()) {
                cadastrarUsuario(nome, email, senha)
            }
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, senha: String) {
        //Salvar dados Firestore
        // será necessário salvar como id, nome, email, foto
        firebaseAuth.createUserWithEmailAndPassword(
            email, senha
        ).addOnCompleteListener { resultado ->
            if (resultado.isSuccessful) {

                val idUsuario = resultado.result.user?.uid
                if (idUsuario != null) {
                    val usuario = Usuario(
                        idUsuario, nome, email
                    )
                    salvarUsuarioListener(usuario)
                }


            }

        }.addOnFailureListener { erro ->
            try {
                throw erro
            } catch (erroSenhaFraca: FirebaseAuthWeakPasswordException) {
                erroSenhaFraca.printStackTrace()
                exibirMensagens("Senha fraca! Favor insira senha com letras, números e caracteres")

            } catch (erroUsuarioExistente: FirebaseAuthUserCollisionException) {
                erroUsuarioExistente.printStackTrace()
                exibirMensagens("Erro! E-mail já cadastado")
            } catch (erroCredenciasInvalidas: FirebaseAuthInvalidCredentialsException) {
                erroCredenciasInvalidas.printStackTrace()
                exibirMensagens("E-mail invalido, favor digite um e-mial válido")
            }
        }
    }

    private fun salvarUsuarioListener(usuario: Usuario) {
        firestore
            .collection("usuarios")
            .document(usuario.id)
            .set(usuario)
            .addOnSuccessListener {
                exibirMensagens("Sucesso ao fazer seu cadastro")
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                )
            }.addOnFailureListener {
                exibirMensagens("Erro ao fazer cadastro")
            }
    }

    //testando se o inputTextLayout está vazio ou não. Neste caso irá demonstrar uma mensagem caso esteja vazio.
    private fun validarCampos(): Boolean {

        nome = binding.editeNome.text.toString()
        email = binding.editeEmail.text.toString()
        senha = binding.editeSenha.text.toString()

        if (nome.isNotEmpty()) {
            binding.textInputLayoutNome.error = null

            if (email.isNotEmpty()) {
                binding.textInputEmail.error = null

                if (senha.isNotEmpty()) {
                    binding.textInputLayoutSenha.error = null
                    return true
                } else {
                    binding.textInputLayoutSenha.error = "Preencha sua Senha"
                    return false
                }

            } else {
                binding.textInputEmail.error = "Preencha seu emamil"
                return false
            }

        } else {
            binding.textInputLayoutNome.error = "Preencha seu nome"
            return false
        }
    }

    fun inicializarToolbar() {
        val toolbar = binding.includeToolbar.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Faça seu cadastro"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
