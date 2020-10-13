package com.example.lexicalanalyzer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {
    TextView textView, t1, error1, error2, error3;
    Button input, parser, output, submit, cancel, okay;
    String einput, space, comment, id, temp;
    String[] str;
    AlertDialog.Builder mBuilder, mBuilder2;
    View mView, mView2;
    ArrayList<String> keywords, kw, identifier, s_constant;
    ArrayList<Integer> n_constant;
    Integer count_kw = 0, count_id = 0, count_const = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keywords = new ArrayList<String>();
        kw = new ArrayList<String>();
        s_constant = new ArrayList<String>();
        n_constant = new ArrayList<Integer>();
        identifier = new ArrayList<String>();

        textView = findViewById(R.id.text);
        t1 = findViewById(R.id.text1);
        error1 = findViewById(R.id.err1);
        error2 = findViewById(R.id.err2);
        error3 = findViewById(R.id.err3);

        input = findViewById(R.id.source_code);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    mBuilder = new AlertDialog.Builder(MainActivity.this);
                    mView = getLayoutInflater().inflate(R.layout.input, null);
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();

                    final EditText code = mView.findViewById(R.id.code);

                    submit = mView.findViewById(R.id.submit);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            einput = String.valueOf(code.getText());
                            if (!einput.isEmpty()) {
                                textView.setText(einput);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "No code entered", Toast.LENGTH_LONG).show();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.dismiss();
                            }
                        }
                    });

                    cancel = mView.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
//
                }
            }
        });
        parser = findViewById(R.id.parser);
        parser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText code = mView.findViewById(R.id.code);
                einput = String.valueOf(code.getText());
                if (!einput.isEmpty()) {

                    //removal of comments
                    comment = einput.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");

                    // removal of whitespace
                    space = comment.replaceAll("\\s+", "");

                    t1.setText(space);

                    // recognizes keywords
                    keywords.add("abstract");
                    keywords.add("byte");
                    keywords.add("continue");
                    keywords.add("else");
                    keywords.add("for");
                    keywords.add("import");
                    keywords.add("new");
                    keywords.add("public");
                    keywords.add("switch");
                    keywords.add("throws");
                    keywords.add("assert");
                    keywords.add("case");
                    keywords.add("default");
                    keywords.add("enum");
                    keywords.add("goto");
                    keywords.add("instanceof");
                    keywords.add("package");
                    keywords.add("return");
                    keywords.add("package");
                    keywords.add("transient");
                    keywords.add("synchronized");
                    keywords.add("catch");
                    keywords.add("boolean");
                    keywords.add("extends");
                    keywords.add("do");
                    keywords.add("int");
                    keywords.add("if");
                    keywords.add("short");
                    keywords.add("private");
                    keywords.add("try");
                    keywords.add("this");
                    keywords.add("char");
                    keywords.add("break");
                    keywords.add("final");
                    keywords.add("double");
                    keywords.add("interface");
                    keywords.add("implements");
                    keywords.add("static");
                    keywords.add("protected");
                    keywords.add("void");
                    keywords.add("throw");
                    keywords.add("class");
                    keywords.add("finally");
                    keywords.add("long");
                    keywords.add("strictfp");
                    keywords.add("const");
                    keywords.add("float");
                    keywords.add("volatile");
                    keywords.add("native");
                    keywords.add("super");
                    keywords.add("while");
                    keywords.add("string");

                    temp = comment.replaceAll(",", " ").replaceAll(";"," ").replaceAll("\n", " ").replaceAll("=", " ");
                    str = temp.split(" ");

                    for (int i = 0; i < str.length; i++) {
                        if (keywords.contains(str[i])) {
                            count_kw += 1;
                            kw.add(str[i]);
                        }
                    }

                    // recognizes identifiers
                    id = "^([a-zA-Z_][a-zA-Z\\\\d_]*)$";
                    for (int j = 0; j < str.length; j++) {
                        for (int z = 0; z < kw.size(); z++) {
                            if (!kw.contains(str[j])) {
                                if (!identifier.contains(str[j])) {
                                    if (str[j].matches(id)) {
                                        identifier.add(str[j]);
                                        count_id += 1;
                                    }
                                }
                            }
                        }
                    }
                    if(count_id==0){
                            error1.setText("Invalid Identifier or no Identifier Found");
                    }

                    // recognizes constants

                    String nc = " [-+]?([0-9]*\\.[0-9]+|[0-9]+)";
                    String string = "((?<![\\\\])['\"])((?:.(?!(?<![\\\\])\\1))*.?)\\1";
                    String sc = "[ \\\\\\\\s@  [\\\\\\\"]\\\\\\\\[\\\\\\\\]\\\\\\\\\\\\|^{#%'*/<()>}:`;,!& .?_$=+-]+";
                    for(int y = 0; y<str.length; y++){
                        if(!keywords.contains(str[y]) && !identifier.contains(str[y]) && !str[y].matches(sc)) {
                            if (!n_constant.contains(str[y]) || !s_constant.contains(str[y])) {
                                if (str[y].matches(string)) {
                                    s_constant.add(str[y]);
                                    count_const += 1;
                                }
                                if (str[y].matches(nc)) {
                                    n_constant.add(Integer.valueOf(str[y]));
                                    count_const += 1;
                                }
                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No source code entered", Toast.LENGTH_LONG).show();
                }
            }

        });

            output = findViewById(R.id.output);
            output.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBuilder2 = new AlertDialog.Builder(MainActivity.this);
                    mView2 = getLayoutInflater().inflate(R.layout.output, null);
                    mBuilder2.setView(mView2);

                    LinearLayout linearLayout = mView2.findViewById(R.id.linear);

                    final AlertDialog dialog2 = mBuilder2.create();
                    if (!einput.isEmpty()) {

                        //Keyword

                        if (count_kw > 0) {
                            for (int k = 0; k < kw.size(); k++) {
                                TextView lexeme = new TextView(MainActivity.this);
                                lexeme.setTextColor(getResources().getColor(R.color.black));
                                lexeme.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                                lexeme.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
                                lexeme.setText("\t\t\t\t\t\t" + kw.get(k) + "\t\t\t\t\t\t\t\t\tKeyword");
                                linearLayout.addView(lexeme);
                            }
                        }

                        //Identifier
                        if(count_id>0) {
                            for (int x = 0; x < identifier.size(); x++) {
                                TextView lexeme2 = new TextView(MainActivity.this);
                                lexeme2.setTextColor(getResources().getColor(R.color.black));
                                lexeme2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                                lexeme2.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
                                lexeme2.setText("\t\t\t\t\t\t" + identifier.get(x) + "\t\t\t\t\t\t\t\t\tIdentifier");
                                linearLayout.addView(lexeme2);
                            }
                        }

                        //Constant
                        if(count_const>0) {
                            for (int m = 0; m < n_constant.size(); m++) {
                                TextView lexeme3 = new TextView(MainActivity.this);
                                lexeme3.setTextColor(getResources().getColor(R.color.black));
                                lexeme3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                                lexeme3.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
                                lexeme3.setText("\t\t\t\t" + n_constant.get(m) + "\t\t\t\t\tNumeric Constant");
                                linearLayout.addView(lexeme3);
                            }
                            for (int n = 0; n < s_constant.size(); n++) {
                                TextView lexeme4 = new TextView(MainActivity.this);
                                lexeme4.setTextColor(getResources().getColor(R.color.black));
                                lexeme4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                                lexeme4.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
                                lexeme4.setText("\t\t\t\t" + s_constant.get(n) + "\t\t\t\t\tString Constant");
                                linearLayout.addView(lexeme4);
                            }
                        }

                        okay = mView2.findViewById(R.id.ok);
                        okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                        dialog2.show();
                    } else {
                        Toast.makeText(MainActivity.this, "No source code or parser used", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
}