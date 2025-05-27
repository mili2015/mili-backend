package br.com.mili.milibackend.fornecedor.infra.converter;


import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class GfdDocumentoStatusEnumConverter implements AttributeConverter<GfdDocumentoStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(GfdDocumentoStatusEnum attribute) {
        return attribute != null ? attribute.getDescricao() : null;
    }

    @Override
    public GfdDocumentoStatusEnum convertToEntityAttribute(String dbData) {
        return dbData != null ? GfdDocumentoStatusEnum.fromDescricao(dbData) : null;
    }
}