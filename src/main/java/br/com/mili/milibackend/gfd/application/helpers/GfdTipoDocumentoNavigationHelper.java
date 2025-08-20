package br.com.mili.milibackend.gfd.application.helpers;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;

import java.util.List;

public class GfdTipoDocumentoNavigationHelper {

    public static GfdDocumentoNavigation calculateNavigation(List<GfdTipoDocumentoGetAllOutputDto> tipos, Integer idDocumento) {
        var tipoDocumento = tipos.stream()
                .filter(tipo -> tipo.getId().equals(idDocumento))
                .findFirst();

        int index = tipoDocumento.map(tipos::indexOf)
                .orElse(-1);

        if (index == -1 || tipos.isEmpty()) {
            return new GfdDocumentoNavigation(0, 0);
        }

        boolean isLast = index == tipos.size() - 1;
        int nextDoc = isLast ? 0 : tipos.get(index + 1).getId();
        int prevDoc = index > 0 ? tipos.get(index - 1).getId() : 0;

        return new GfdDocumentoNavigation(nextDoc, prevDoc);
    }
}