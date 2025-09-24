package br.com.mili.milibackend.gfd.application.dto.manager.fornecedor;

import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByIdOutputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdMFornecedorGetOutputDto {
    private Integer codigo;

    private String cgcCpf;

    private Integer ordemCgc;

    private String razaoSocial;

    private String nomeFantasia;

    private String endereco;

    private String bairro;

    private String cidade;

    private String estado;

    private String celular;

    private String cep;

    private String ie;

    private String telefone;

    private String observacao;

    private String fone1;

    private String fone2;

    private String uf;

    private String contato;

    private String email;

    private Integer codNaturezaFornecedor;

    private String usuario;

    private String codCnae;

    private String tipoConta;

    private String email2;

    private Integer codPais;

    private String inscricaoMunicipal;

    private String bloqueado;

    private Integer codUsuario;

    private Integer aceiteLgpd;

    private GfdTipoFornecedorDto tipoFornecedor;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class GfdTipoFornecedorDto {
        private Integer id;
        private String descricao;
    }
}
