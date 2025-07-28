package br.com.mili.milibackend.envioEmail.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "FILA_ENVIO_EMAIL2")
public class EnvioEmail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NUMERO_EMAIL2")
    @SequenceGenerator(name = "SEQ_NUMERO_EMAIL2", sequenceName = "SEQ_NUMERO_EMAIL2", allocationSize = 1)
    @Column(name = "NUMERO", nullable = false)
    private Long numero;
    
    @Column(name = "REMETENTE")
    private String remetente;
    
    @Column(name = "DESTINATARIO")
    private String destinatario;
    
    @Column(name = "ASSUNTO")
    private String assunto;
    
    @Column(name = "TITULO")
    private String titulo;
    
    @Column(name = "TEXTO")
    private String texto;
    
    @Column(name = "DATA_ENTRADA")
    private LocalDateTime dataEntrada;

    
    @PrePersist
    public void prePersist() {
        if (this.dataEntrada == null) {
            this.dataEntrada = LocalDateTime.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvioEmail that = (EnvioEmail) o;
        return Objects.equals(numero, that.numero);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
    
    @Override
    public String toString() {
        return "EnvioEmail{" +
                "numero=" + numero +
                ", remetente='" + remetente + '\'' +
                ", assunto='" + assunto + '\'' +
                ", dataEntrada=" + dataEntrada +
                '}';
    }
}
