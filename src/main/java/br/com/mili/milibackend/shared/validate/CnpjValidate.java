package br.com.mili.milibackend.shared.validate;

import br.com.mili.milibackend.shared.util.Util;

public class CnpjValidate implements IValidate{

    public boolean isValido(String s_aux) {

        if (s_aux == null)
            return false;

        if(! Util.isStringContainsOnlyDigits(s_aux))
            return false;

        if (s_aux.length() != 14)
            return false;

        int soma = 0, aux, dig;
        String cnpj_calc = s_aux.substring(0,12);
        char[] chr_cnpj = s_aux.toCharArray();
        //--------- Primeira parte
        for( int i = 0; i < 4; i++ )
            if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 )
                soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
        for( int i = 0; i < 8; i++ )
            if ( chr_cnpj[i+4]-48 >=0 && chr_cnpj[i+4]-48 <=9 )
                soma += (chr_cnpj[i+4] - 48) * (10 - (i + 1));
        dig = 11 - (soma % 11);
        cnpj_calc += ( dig == 10 || dig == 11 ) ?
                "0" : Integer.toString(dig);
        //--------- Segunda parte
        soma = 0;
        for ( int i = 0; i < 5; i++ )
            if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 )
                soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
        for ( int i = 0; i < 8; i++ )
            if ( chr_cnpj[i+5]-48 >=0 && chr_cnpj[i+5]-48 <=9 )
                soma += (chr_cnpj[i+5] - 48) * (10 - (i + 1));
        dig = 11 - (soma % 11);
        cnpj_calc += ( dig == 10 || dig == 11 ) ?
                "0" : Integer.toString(dig);
        return s_aux.equals(cnpj_calc);
    }
}
