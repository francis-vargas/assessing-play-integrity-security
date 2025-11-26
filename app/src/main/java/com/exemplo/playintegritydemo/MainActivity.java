package com.exemplo.playintegritydemo;

import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityTokenRequest;
import com.google.android.play.core.integrity.StandardIntegrityManager;
import com.google.android.play.core.integrity.StandardIntegrityManager.PrepareIntegrityTokenRequest;
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityToken;
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityTokenProvider;
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityTokenRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String BACKEND_URL = "https://knickerbockered-oakley-ipsilateral.ngrok-free.dev/verify";
    private static final long CLOUD_PROJECT_NUMBER = 53044832944L;
    private TextView resultadoTextView;
    private OkHttpClient client = new OkHttpClient();
    private StandardIntegrityTokenProvider standardProvider;
    private IntegrityManager integrityManager;
    private StandardIntegrityManager standardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultadoTextView = findViewById(R.id.resultadoTextView);
        Button btnClassico = findViewById(R.id.btnClassico);
        Button btnPadrao = findViewById(R.id.btnPadrao);
        Button btnCombinado = findViewById(R.id.btnCombinado);

        integrityManager = IntegrityManagerFactory.create(getApplicationContext());
        standardManager = IntegrityManagerFactory.createStandard(getApplicationContext());

        PrepareIntegrityTokenRequest standardRequest = PrepareIntegrityTokenRequest.builder()
                .setCloudProjectNumber(CLOUD_PROJECT_NUMBER)
                .build();

        standardManager.prepareIntegrityToken(standardRequest)
                .addOnSuccessListener(provider -> standardProvider = provider)
                .addOnFailureListener(e -> runOnUiThread(() ->
                        resultadoTextView.setText("Erro ao preparar provedor padrão: " + e.getMessage())
                ));

        btnClassico.setOnClickListener(v -> solicitarTokenClassico());
        btnPadrao.setOnClickListener(v -> solicitarTokenPadrao());
        btnCombinado.setOnClickListener(v -> solicitarTokenCombinado());
    }

    private void solicitarTokenClassico() {
        //String nonce = UUID.randomUUID().toString();
        String nonce = "6ZEiaVwMr_AzzTSJEThOZkbGK2uas6z5F3ZusnUiatg"; // valor fixo para teste de replay

        IntegrityTokenRequest request = IntegrityTokenRequest.builder()
                .setCloudProjectNumber(CLOUD_PROJECT_NUMBER)
                .setNonce(nonce)
                .build();

        integrityManager.requestIntegrityToken(request)
                .addOnSuccessListener(response -> enviarParaBackend(response.token(), "classico"))
                .addOnFailureListener(e -> resultadoTextView.setText("Erro clássico: " + e.getMessage()));
    }

    private void solicitarTokenPadrao() {
        if (standardProvider == null) {
            resultadoTextView.setText("Provedor padrão ainda não está pronto.");
            return;
        }

        //String mensagem = UUID.randomUUID().toString();
        String mensagem = "teste-replay-123";
        String nonceBase64 = calcularHash(mensagem, true);

        StandardIntegrityTokenRequest request = StandardIntegrityTokenRequest.builder()
                .setRequestHash(nonceBase64)
                .setVerdictOptOut(Collections.emptySet())
                .build();

        Task<StandardIntegrityToken> task = standardProvider.request(request);
        task.addOnSuccessListener(response -> enviarParaBackend(response.token(), "padrao"))
                .addOnFailureListener(e -> resultadoTextView.setText("Erro padrao: " + e.getMessage()));
    }

    private void solicitarTokenCombinado() {
        //String valorUnico = UUID.randomUUID().toString();
        String valorUnico = "teste-replay-123"; // valor fixo para teste de replay
        String mensagem = "acao_valiosa:" + valorUnico;
        String hashNonce = calcularHash(mensagem, true);

        IntegrityTokenRequest request = IntegrityTokenRequest.builder()
                .setNonce(hashNonce)
                .setCloudProjectNumber(CLOUD_PROJECT_NUMBER)
                .build();

        integrityManager.requestIntegrityToken(request)
                .addOnSuccessListener(response -> enviarParaBackend(response.token(), "combinado", valorUnico))
                .addOnFailureListener(e -> resultadoTextView.setText("Erro combinado: " + e.getMessage()));
    }

    private void enviarParaBackend(String token, String tipo) {
        enviarParaBackend(token, tipo, null);
    }

    private void enviarParaBackend(String token, String tipo, String valorUnico) {
        try {
            JSONObject json = new JSONObject();
            json.put("integrity_token", token);
            json.put("tipo_requisicao", tipo);
            if (valorUnico != null) json.put("valor_unico", valorUnico);

            RequestBody body = RequestBody.create(
                    json.toString(), MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(BACKEND_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> resultadoTextView.setText("Erro envio: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    runOnUiThread(() -> resultadoTextView.setText("Resposta: " + body));
                }
            });

        } catch (Exception e) {
            resultadoTextView.setText("Erro JSON: " + e.getMessage());
        }
    }

    private String calcularHash(String input, boolean urlSafe) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            int flags = urlSafe ? Base64.URL_SAFE | Base64.NO_WRAP : Base64.NO_WRAP;
            String encoded = Base64.encodeToString(hash, flags);
            return encoded.replace("=", ""); // remove padding se houver
        } catch (NoSuchAlgorithmException e) {
            return "erro-hash";
        }
    }

}
