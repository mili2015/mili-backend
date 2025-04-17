package br.com.mili.milibackend.shared.validate;

import br.com.mili.milibackend.shared.util.Util;

public class CpfValidate implements IValidate{

    public boolean isValido(String s_aux) {

        if (s_aux == null)
            return false;

        if(! Util.isStringContainsOnlyDigits(s_aux))
            return false;

        if (s_aux.length() != 11)
            return false;
    //------- Rotina para CPF
        int d1, d2;
        int digito1, digito2, resto;
        int digitoCPF;
        String nDigResult;
        d1 = d2 = 0;
        digito1 = digito2 = resto = 0;
        for (int n_Count = 1; n_Count < s_aux.length() - 1; n_Count++) {
            digitoCPF = Integer.valueOf(s_aux.substring(n_Count - 1, n_Count)).intValue();
            //--------- Multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
            d1 = d1 + (11 - n_Count) * digitoCPF;
            //--------- Para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
            d2 = d2 + (12 - n_Count) * digitoCPF;
        }
        ;
        //--------- Primeiro resto da divis�o por 11.
        resto = (d1 % 11);
        //--------- Se o resultado for 0 ou 1 o digito � 0 caso contr�rio o digito � 11 menos o resultado anterior.
        if (resto < 2)
            digito1 = 0;
        else
            digito1 = 11 - resto;
        d2 += 2 * digito1;
        //--------- Segundo resto da divis�o por 11.
        resto = (d2 % 11);
        //--------- Se o resultado for 0 ou 1 o digito � 0 caso contr�rio o digito � 11 menos o resultado anterior.
        if (resto < 2)
            digito2 = 0;
        else
            digito2 = 11 - resto;
        //--------- Digito verificador do CPF que est� sendo validado.
        String nDigVerific = s_aux.substring(s_aux.length() - 2, s_aux.length());
        //--------- Concatenando o primeiro resto com o segundo.
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
        //--------- Comparar o digito verificador do cpf com o primeiro resto + o segundo resto.
        return nDigVerific.equals(nDigResult);
    }
}
