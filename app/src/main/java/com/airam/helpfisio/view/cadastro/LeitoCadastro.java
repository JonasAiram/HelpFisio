package com.airam.helpfisio.view.cadastro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.airam.helpfisio.R;
import com.airam.helpfisio.controller.HospitalController;
import com.airam.helpfisio.controller.LeitoController;
import com.airam.helpfisio.model.DateUtil;
import com.airam.helpfisio.model.Hospital;
import com.airam.helpfisio.model.Leito;
import com.airam.helpfisio.view.LeitoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 18/11/2017.
 */

public class LeitoCadastro implements DialogInterface.OnShowListener, View.OnClickListener, DialogInterface.OnDismissListener, AdapterView.OnItemSelectedListener{

    private HospitalController hospitalController;
    private LeitoController leitoController;
    private Leito leito;

    private AlertDialog dialog;

    private EditText editTextTipo, editTextQtd, editTextChefe, editTextAndar, editTextData;
    private EditText editTextMedico, editTextFisio, editTextCusto, editTextOrcamento;
    private Spinner spnHospId;

    private int idHospital;

    private List<String> listaNomeHospital = new ArrayList<String>();
    List<Hospital> listHospital;

    Context context;

    boolean criadoComSucesso;

    public LeitoCadastro(Context context) {

        //CRIA O CONTEXT
        this.context = context;

        leitoController = new LeitoController(context);

        //CRIA O LAYOUT COMO ALERTDIALOG
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.leitocadastro, null);
        builder.setView(view);

        //ATRIBUI AS VARIVEIS AOS ITENS DO LAYOUT
        editTextTipo        = (EditText) view.findViewById(R.id.edtLeitoTipo);
        editTextQtd         = (EditText) view.findViewById(R.id.edtLeitoQtd);
        editTextChefe       = (EditText) view.findViewById(R.id.edtLeitoChefe);
        editTextAndar       = (EditText) view.findViewById(R.id.edtLeitoAndar);
        editTextData        = (EditText) view.findViewById(R.id.edtLeitoData);
        editTextMedico      = (EditText) view.findViewById(R.id.edtLeitoMedico);
        editTextFisio       = (EditText) view.findViewById(R.id.edtLeitoFisio);
        editTextCusto       = (EditText) view.findViewById(R.id.edtLeitoCusto);
        editTextOrcamento   = (EditText) view.findViewById(R.id.edtLeitoOrcamento);
        spnHospId           = (Spinner)  view.findViewById(R.id.spnLeitoIdHospital);

        //SPINNER
        hospitalController = new HospitalController(context);
        arrayIdHospital();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listaNomeHospital);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnHospId.setAdapter(adapter);

        spnHospId.setOnItemSelectedListener(this);

        //CRIA OS BUTTONS DO ALERTDIALOG
        builder.setPositiveButton("Salvar", null);
        builder.setNegativeButton("Voltar", null);

        //START O ALERTDIALOG
        dialog = builder.create();
        dialog.setOnShowListener(this);
        dialog.show();

    }

    private void arrayIdHospital() {

        listHospital = hospitalController.getAll();
        for (Hospital hospital : listHospital){
            listaNomeHospital.add(hospital.getNome() + " Cidade: " + hospital.getCidade());
        }

    }

    public void loadLeito(Leito leito){

        this.leito = leito;

        spnHospId.setSelection(getIndexHospitalId(leito.getId_Hospital()));

        editTextTipo.setText(leito.getTipo());
        editTextQtd.setText(String.valueOf(leito.getQuantidade()));
        editTextChefe.setText(leito.getChefe());
        editTextAndar.setText(String.valueOf(leito.getAndar()));
        editTextData.setText(DateUtil.dateToString(leito.getData()));
        editTextMedico.setText(leito.getMedicoResp());
        editTextFisio.setText(leito.getFisioResp());
        editTextCusto.setText(String.valueOf(leito.getCustoSemanal()));
        editTextOrcamento.setText(String.valueOf(leito.getOrcamentoMesal()));


    }

    private int getIndexHospitalId(int idHospital){
        for (int index = 0; index < listHospital.size(); index++){
            Hospital hospital = listHospital.get(index);
            if (idHospital == hospital.getId())
                return index;
        }
        return 0;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {

        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        b.setId(DialogInterface.BUTTON_POSITIVE);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        insertLeito();

        if (criadoComSucesso) {
            Toast.makeText(context, "Leito Armazenado Com Sucesso.", Toast.LENGTH_SHORT).show();
            ((LeitoView) context).atualizarRegistros();
        } else
            Toast.makeText(context, "Não Foi Possivel Armazenar o Leito.", Toast.LENGTH_SHORT).show();

        dialog.dismiss();

    }

    public  void insertLeito(){

        //ATRIBUIÇÃO DAS VARIAVEIS PARA STRINGS PARA FACILITAR NA ESTRUTURA DE CONDIÇÃO IF
        String tipo = editTextTipo.getText().toString();
        String qtd = editTextQtd.getText().toString();
        String chefe = editTextChefe.getText().toString();
        String andar = editTextAndar.getText().toString();
        String data = editTextData.getText().toString();
        String medico = editTextMedico.getText().toString();
        String fisio = editTextFisio.getText().toString();
        String custo = editTextCusto.getText().toString();
        String orcamento = editTextOrcamento.getText().toString();

        //APRESENTA OS ERROS AO DEIXAR ALGUM ATRIBUTO EM BRANCO
        if (tipo.length() == 0)
            editTextTipo.setError("Digite o Tipo do Leito!");
        if (qtd.length() == 0)
            editTextQtd.setError("Digite a Quantidade!");
        if (chefe.length() == 0)
            editTextChefe.setError("Digite o Nome do Chefe!");
        if (andar.length() == 0)
            editTextAndar.setError("Digite o Andar do Leito!");
        if (data.length() == 0)
            editTextData.setError("Digite a Data do Leito!");
        if (medico.length() == 0)
            editTextMedico.setError("Digite o Médico Responsável!");
        if (fisio.length() == 0)
            editTextFisio.setError("Digite o Fisio Responsável!");
        if (custo.length() == 0)
            editTextCusto.setError("Digite o Custo Semanal!");
        if (orcamento.length() == 0)
            editTextOrcamento.setError("Digite o Orçamento Mensal!");

        //SE TODOS OS CAMPOS FOREM PREENCHIDOS SERÁ EXECUTADA ESTÁ AÇÃO
        if (tipo.length() != 0 && qtd.length() != 0 && chefe.length() != 0 && andar.length() != 0){

            if (leito == null) {


                int qtdLeito = Integer.parseInt(qtd);
                int andarLeito = Integer.parseInt(andar);
                double custoDouble = Double.parseDouble(custo);
                double orcamentoDouble = Double.parseDouble(orcamento);

                //REGRAS PARA ARMAZENAR NO BANCO DE DADOS
                Leito leito = new Leito();
                leito.setTipo(tipo);
                leito.setQuantidade(qtdLeito);
                leito.setChefe(chefe);
                leito.setAndar(andarLeito);
                leito.setId_Hospital(idHospital);
                leito.setData(DateUtil.stringToDate(data));
                leito.setMedicoResp(medico);
                leito.setFisioResp(fisio);
                leito.setCustoSemanal(custoDouble);
                leito.setOrcamentoMesal(orcamentoDouble);

                criadoComSucesso = leitoController.insert(leito);
            }else {

                int qtdLeito = Integer.parseInt(qtd);
                int andarLeito = Integer.parseInt(andar);
                double custoDouble = Double.parseDouble(custo);
                double orcamentoDouble = Double.parseDouble(orcamento);

                //REGRAS PARA ARMAZENAR NO BANCO DE DADOS
                leito.setTipo(tipo);
                leito.setQuantidade(qtdLeito);
                leito.setChefe(chefe);
                leito.setAndar(andarLeito);
                leito.setId_Hospital(idHospital);
                leito.setData(DateUtil.stringToDate(data));
                leito.setMedicoResp(medico);
                leito.setFisioResp(fisio);
                leito.setCustoSemanal(custoDouble);
                leito.setOrcamentoMesal(orcamentoDouble);
                leitoController.edit(leito, leito.getId());
                criadoComSucesso = true;

            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface){
        leitoController.closeDb();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Hospital hospital = listHospital.get(i);
        idHospital = hospital.getId();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
