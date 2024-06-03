package com.example.aulawhatsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aulawhatsapp.databinding.ActivityLoginBinding
import com.example.aulawhatsapp.utils.exibirMensagens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var senha: String

    //Firebase
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarEventosClique()
        //firebaseAuth.signOut()
    }

    override fun onStart() {
        super.onStart()
        verifcarUsuarioLogado()
    }

    private fun verifcarUsuarioLogado() {
        val usuarioAtual = firebaseAuth.currentUser
        if(usuarioAtual != null){
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    private fun inicializarEventosClique() {
        binding.textCadastro.setOnClickListener {
            startActivity(
                Intent(this, CadastroActivity::class.java)
            )
        }
        binding.btnLogar.setOnClickListener {
            if (validarCampos()) {
                logarUsuario()
            }
        }
    }

    private fun logarUsuario() {
        firebaseAuth.signInWithEmailAndPassword(
            email, senha
        ).addOnSuccessListener {
            exibirMensagens("Logado com sucesso")
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }.addOnFailureListener {erro ->
            try {
                throw erro
            }catch (erroUsuarioInvalido: FirebaseAuthInvalidUserException){
                erroUsuarioInvalido.printStackTrace()
                exibirMensagens("E-mail não cadastrado")
            }catch (erroCredenciasInvalidas: FirebaseAuthInvalidUserException){
                erroCredenciasInvalidas.printStackTrace()
                exibirMensagens("E-mail ou Senha estão incorretos")
            }
        }
    }

    private fun validarCampos(): Boolean {
        email = binding.editLoginEmail.text.toString()
        senha = binding.editLoginSenha.text.toString()

        if (email.isNotEmpty()) {
            binding.textInputLayoutLoginSenha.error = null
            if (senha.isNotEmpty()) {
                binding.textInputLayoutLoginSenha.error = null
                return true
            } else {
                binding.textInputLayoutLoginSenha.error = "Digite sua senha"
                return false
            }
        } else {
            binding.textInputLayoutEmail.error = "Preencha seu email"
            return false
        }
    }

}