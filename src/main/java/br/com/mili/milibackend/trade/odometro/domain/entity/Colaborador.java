package br.com.mili.milibackend.trade.odometro.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MCD_COLABORADOR")
public class Colaborador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MCD_COLABORADOR")
    @SequenceGenerator(name = "SEQ_MCD_COLABORADOR", sequenceName = "SEQ_MCD_COLABORADOR", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;
    
    @Column(name = "ID_COLABORADOR_SUPERIOR")
    private Integer idColaboradorSuperior;
    
    @Column(name = "MCDA_CODIGO", nullable = false)
    private Integer mcdaCodigo;
    
    @Column(name = "CTUSU_CODIGO", nullable = false)
    private Integer ctusuCodigo;
    
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;
    
    @Column(name = "SOBRENOME", length = 100)
    private String sobrenome;
    
    @Column(name = "CPF", length = 15)
    private String cpf;
    
    @Column(name = "RG", length = 20)
    private String rg;
    
    @Column(name = "EMAIL", nullable = false, length = 150)
    private String email;
    
    @Column(name = "CELULAR", nullable = false, length = 20)
    private String celular;
    
    @Column(name = "UF", length = 2)
    private String uf;
    
    @Column(name = "CIDADE", length = 100)
    private String cidade;
    
    @Column(name = "ENDERECO", length = 150)
    private String endereco;
    
    @Column(name = "NUMERO", length = 10)
    private String numero;
    
    @Column(name = "COMPLEMENTO", length = 255)
    private String complemento;
    
    @Column(name = "BAIRRO", length = 50)
    private String bairro;
    
    @Column(name = "CEP", length = 10)
    private String cep;
    
    @Column(name = "HORAS_SEMANAIS")
    private Integer horasSemanais;
    
    @Column(name = "FUSO_HORARIO", length = 150)
    private String fusoHorario;
    
    @Column(name = "LATITUDE", length = 50)
    private String latitude;
    
    @Column(name = "LONGITUDE", length = 50)
    private String longitude;
    
    @Column(name = "ATIVO", length = 1)
    private String ativo;
    
    @Column(name = "SENHA", length = 50)
    private String senha;
    
    @Column(name = "PERFIL_ACESSO", nullable = false, length = 20)
    private String perfilAcesso;
    
    @Column(name = "DATA_NASCIMENTO")
    private LocalDate dataNascimento;
    
    @Column(name = "GENERO", length = 20)
    private String genero;
    
    @Column(name = "PATH_IMG_PERFIL", length = 80)
    private String pathImgPerfil;
    
    @Column(name = "TIPO_VINCULO", length = 20)
    private String tipoVinculo;
    
    @Column(name = "CTFUN_CODIGO")
    private Integer ctfunCodigo;
    
    @Column(name = "IDSETOR", length = 4)
    private String idSetor;
    
    @Column(name = "MEIO_TRANSPORTE", length = 10)
    private String meioTransporte;
    
    @OneToMany(mappedBy = "colaborador", fetch = FetchType.LAZY)
    private List<Odometro> odometroReadings;
}
