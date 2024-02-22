package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.Authenticator;

public class FormLogin extends AppCompatActivity implements View.OnClickListener{


    private TextView text_tela_cadastro;
    private EditText edit_email;
    private EditText edit_senha;
    private Button buttonEntrar;
    private ProgressBar progressBar;

    String[] mensagens = {"Preencha todos os campos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressBar);

        buttonEntrar = findViewById(R.id.bt_entrar);
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        text_tela_cadastro.setOnClickListener(this);
        buttonEntrar.setOnClickListener(this);

    }

    private void AutenticarUsuario(View v){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    },3000);
                } else {
                    String erro;

                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao logar o usuário";
                        Snackbar snackbar = Snackbar.make(v, erro,Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(this, TelaPrincipal.class);
        startActivity(intent);
        finish();
    }

    protected  void onStart(){
        super.onStart();

        FirebaseUser useuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (useuarioAtual != null){
            TelaPrincipal();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_tela_cadastro) {
            Intent telaCadastro = new Intent(this, FormCadastro.class);
            startActivity(telaCadastro);
            finish();
        } else if (v.getId() == R.id.bt_entrar) {
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mensagens[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                AutenticarUsuario(v);
            }
        }
    }
}