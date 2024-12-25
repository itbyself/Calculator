package easy.tuto.easycalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultTv, solutionTv;
    private MaterialButton[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        // Инициализация кнопок
        int[] buttonIds = {
                R.id.button_ac, R.id.button_c, R.id.button_open_bracket, R.id.button_close_bracket,
                R.id.button_divide, R.id.button_multiply, R.id.button_plus, R.id.button_minus,
                R.id.button_equals, R.id.button_0, R.id.button_1, R.id.button_2,
                R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6,
                R.id.button_7, R.id.button_8, R.id.button_9, R.id.button_dot
        };

        buttons = new MaterialButton[buttonIds.length];

        for (int i = 0; i < buttonIds.length; i++) {
            buttons[i] = findViewById(buttonIds[i]);
            buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String currentData = solutionTv.getText().toString();

        switch (buttonText) {
            case "AC":
                clearAll();
                break;
            case "=":
                calculateResult(currentData);
                break;
            case "C":
                removeLastCharacter(currentData);
                break;
            default:
                updateCalculation(currentData, buttonText);
                break;
        }
    }

    private void clearAll() {
        solutionTv.setText("");
        resultTv.setText("0");
    }

    private void calculateResult(String data) {
        String result = getResult(data);
        if (!result.equals("Err")) {
            resultTv.setText(result);
            solutionTv.setText(result); // Отображаем итоговое значение в решении
        }
    }

    private void removeLastCharacter(String data) {
        if (!data.isEmpty()) {
            data = data.substring(0, data.length() - 1);
            solutionTv.setText(data);
        }
    }

    private void updateCalculation(String currentData, String buttonText) {
        currentData += buttonText;
        solutionTv.setText(currentData);
    }

    private String getResult(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        } catch (Exception e) {
            return "Err";
        } finally {
            Context.exit();
        }
    }
}
