package com.airam.helpfisio.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.airam.helpfisio.model.DateUtil;
import com.airam.helpfisio.model.Hospital;
import com.airam.helpfisio.model.Leito;

/**
 * Created by Jonas on 18/11/2017.
 */

public class LeitoController extends BaseController<Leito>{

    public LeitoController(Context context){
        //Integrando o banco de dados
        super(context);

    }
    
    //INSERT
    public boolean insert(Leito leito){
        boolean IsCreate = db.insert(Leito.TABLE, null, convertToContentValue(leito)) > 0;
        return IsCreate;
    }

    @Override
    protected Leito convertToObject(Cursor c) {
        Leito leito = new Leito();

        int columnId = c.getColumnIndex(Leito.COLUMN_ID);
        leito.setId(c.getInt(columnId));

        columnId = c.getColumnIndex(Leito.COLUMN_TIPO);
        leito.setTipo(c.getString(columnId));

        columnId = c.getColumnIndex(Leito.COLUMN_QUANTIDADE);
        leito.setQuantidade(c.getInt(columnId));

        columnId = c.getColumnIndex(Leito.COLUMN_CHEFE);
        leito.setChefe(c.getString(columnId));

        columnId = c.getColumnIndex(Leito.COLUMN_ANDAR);
        leito.setAndar(c.getInt(columnId));

        columnId = c.getColumnIndex(Leito.COLUMN_ID_HOSPIAL);
        leito.setId_Hospital(c.getInt(columnId));

        columnId = c.getColumnIndex(Leito.COLUMN_DATA);
        leito.setData(DateUtil.stringToDate(c.getString(columnId)));

        columnId = c.getColumnIndex(Leito.COLUMN_MEDICORESP);
        leito.setMedicoResp(c.getString(columnId));

        columnId = c.getColumnIndex(Leito.COLUMN_FISIORESP);
        leito.setFisioResp(c.getString(columnId));

        columnId = c.getColumnIndex(Leito.COLUMN_CUSTOSEMANAL);
        leito.setCustoSemanal(c.getDouble(columnId));

        columnId = c.getColumnIndex(Leito.COLUMN_ORCAMENTOMENSAL);
        leito.setOrcamentoMesal(c.getDouble(columnId));

        return leito;
    }

    @Override
    protected String[] getColumns() {
        return new String[]{Leito.COLUMN_ID, Leito.COLUMN_TIPO, Leito.COLUMN_QUANTIDADE,
                Leito.COLUMN_CHEFE, Leito.COLUMN_ANDAR, Leito.COLUMN_ID_HOSPIAL, Leito.COLUMN_DATA, Leito.COLUMN_MEDICORESP,
                Leito.COLUMN_FISIORESP, Leito.COLUMN_CUSTOSEMANAL, Leito.COLUMN_ORCAMENTOMENSAL};
    }

    protected ContentValues convertToContentValue(Leito leito) {
        ContentValues values = new ContentValues();

        values.put(Leito.COLUMN_TIPO, leito.getTipo());
        values.put(Leito.COLUMN_QUANTIDADE, leito.getQuantidade());
        values.put(Leito.COLUMN_CHEFE, leito.getChefe());
        values.put(Leito.COLUMN_ANDAR, leito.getAndar());
        values.put(Leito.COLUMN_ID_HOSPIAL, leito.getId_Hospital());
        values.put(Leito.COLUMN_DATA, DateUtil.dateToString(leito.getData()));
        values.put(Leito.COLUMN_MEDICORESP, leito.getMedicoResp());
        values.put(Leito.COLUMN_FISIORESP, leito.getFisioResp());
        values.put(Leito.COLUMN_CUSTOSEMANAL, leito.getCustoSemanal());
        values.put(Leito.COLUMN_ORCAMENTOMENSAL, leito.getOrcamentoMesal());

        return values;
    }

    @Override
    protected String getTable() {
        return Leito.TABLE;
    }

    @Override
    protected String getColumnId(){
        return Leito.COLUMN_ID;
    }

    public Hospital buscarNomePeloId(int hospitalId){

        Hospital hospital = new Hospital();

        String sql = "SELECT * FROM hospital WHERE _id = " + hospitalId;
         Cursor c = db.rawQuery(sql, null);
         if (c.moveToFirst()){

             String nome = c.getString(c.getColumnIndex("nome"));

             hospital = new Hospital();

             hospital.setId(hospitalId);
             hospital.setNome(nome);
         }
         return hospital;
    }
}
