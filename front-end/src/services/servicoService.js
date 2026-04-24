import { apiRequest } from './api'

export function criarPropostaInicial(payload) {
  return apiRequest('/servicos/criar', {
    method: 'POST',
    body: payload,
  })
}

