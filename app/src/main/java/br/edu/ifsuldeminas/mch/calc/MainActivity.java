package br.edu.ifsuldeminas.mch.calc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CalculadoraApp";
    private TextView textViewResultado, textViewUltimaExpressao;
    private StringBuilder expressaoAtual = new StringBuilder();     // para cálculo
    private StringBuilder expressaoVisivel = new StringBuilder();   // para exibição
    private boolean ultimoFoiResultado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResultado = findViewById(R.id.textViewResultadoID);
        textViewUltimaExpressao = findViewById(R.id.textViewUltimaExpressaoID);

        configurarBotoes();
        configurarFuncoes();
    }

    private void configurarBotoes() {
        int[] botoesNumericos = {
                R.id.buttonZeroID, R.id.buttonUmID, R.id.buttonDoisID,
                R.id.buttonTresID, R.id.buttonQuatroID, R.id.buttonCincoID,
                R.id.buttonSeisID, R.id.buttonSeteID, R.id.buttonOitoID,
                R.id.buttonNoveID
        };

        int[] botoesOperadores = {
                R.id.buttonSomaID, R.id.buttonSubtracaoID,
                R.id.buttonMultiplicacaoID, R.id.buttonDivisaoID,
                R.id.buttonPorcentoID
        };

        View.OnClickListener listenerNumerico = v -> {
            Button b = (Button) v;
            if (ultimoFoiResultado) {
                expressaoAtual.setLength(0);
                expressaoVisivel.setLength(0);
                textViewUltimaExpressao.setText("");
                ultimoFoiResultado = false;
            }
            expressaoAtual.append(b.getText());
            expressaoVisivel.append(b.getText());
            textViewResultado.setText(expressaoVisivel.toString());
        };

        View.OnClickListener listenerOperador = v -> {
            Button b = (Button) v;
            String op = b.getText().toString();
            String calcOp = op;

            if (op.equals("÷")) calcOp = "/";
            if (op.equals("×")) calcOp = "*";
            if (op.equals("%")) calcOp = "*0.01";

            if (ultimoFoiResultado) {
                expressaoAtual.setLength(0);
                expressaoVisivel.setLength(0);
                expressaoAtual.append(textViewResultado.getText().toString());
                expressaoVisivel.append(textViewResultado.getText().toString());
                ultimoFoiResultado = false;
            }

            expressaoAtual.append(calcOp);
            expressaoVisivel.append(op);
            textViewResultado.setText(expressaoVisivel.toString());
        };

        for (int id : botoesNumericos)
            findViewById(id).setOnClickListener(listenerNumerico);

        for (int id : botoesOperadores)
            findViewById(id).setOnClickListener(listenerOperador);

        Button buttonPonto = findViewById(R.id.buttonVirgulaID);
        buttonPonto.setOnClickListener(v -> {
            if (ultimoFoiResultado) {
                expressaoAtual.setLength(0);
                expressaoVisivel.setLength(0);
                textViewUltimaExpressao.setText("");
                ultimoFoiResultado = false;
            }
            expressaoAtual.append(".");
            expressaoVisivel.append(".");
            textViewResultado.setText(expressaoVisivel.toString());
        });
    }

    private void configurarFuncoes() {
        Button buttonIgual = findViewById(R.id.buttonIgualID);
        Button buttonReset = findViewById(R.id.buttonResetID);
        Button buttonDelete = findViewById(R.id.buttonDeleteID);

        buttonIgual.setOnClickListener(view -> {
            try {
                String expressao = expressaoAtual.toString();
                Expression e = new ExpressionBuilder(expressao).build();
                double resultado = e.evaluate();

                textViewUltimaExpressao.setText(expressaoVisivel.toString());
                textViewResultado.setText(String.valueOf(resultado));

                expressaoAtual.setLength(0);
                expressaoVisivel.setLength(0);
                ultimoFoiResultado = true;
            } catch (Exception e) {
                Log.e(TAG, "Erro ao calcular: " + e.getMessage());
                textViewResultado.setText("Erro");
                ultimoFoiResultado = false;
            }
        });

        buttonReset.setOnClickListener(v -> {
            expressaoAtual.setLength(0);
            expressaoVisivel.setLength(0);
            textViewResultado.setText("0");
            textViewUltimaExpressao.setText("");
            ultimoFoiResultado = false;
        });

        buttonDelete.setOnClickListener(v -> {
            if (expressaoAtual.length() > 0 && expressaoVisivel.length() > 0) {
                expressaoAtual.deleteCharAt(expressaoAtual.length() - 1);
                expressaoVisivel.deleteCharAt(expressaoVisivel.length() - 1);
                textViewResultado.setText(expressaoVisivel.toString());
            }
        });
    }
}
