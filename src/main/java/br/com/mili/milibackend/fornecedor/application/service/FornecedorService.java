package br.com.mili.milibackend.fornecedor.application.service;


import br.com.mili.milibackend.fornecedor.application.dto.FornecedoGetByCodUsuarioInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedoGetByCodUsuarioOutputDto;
import br.com.mili.milibackend.fornecedor.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.repository.FornecedorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ModelMapper modelMapper;


    public FornecedoGetByCodUsuarioOutputDto getByCodUsuario(FornecedoGetByCodUsuarioInputDto inputDto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodUsuario(inputDto.getCodUsuario());

        //todo: colocar por cod usuario
        Fornecedor fornecedorFound = fornecedorRepository.findById(fornecedor.getCodUsuario()).orElse(null);

        if (fornecedorFound == null) {
            return null;
        }

        return modelMapper.map(fornecedorFound, FornecedoGetByCodUsuarioOutputDto.class);
    }
}
