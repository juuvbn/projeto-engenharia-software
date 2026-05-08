import { apiRequest } from './api'

export function listarServicosDoCliente({ busca, status, page = 0, size = 20 } = {}) {
  const params = new URLSearchParams()
  if (busca) params.append('busca', busca)
  if (status?.length) status.forEach(s => params.append('status', s))
  params.append('page', String(page))
  params.append('size', String(size))
  return apiRequest(`/clientes/me/servicos?${params.toString()}`, { method: 'GET' })
}

export function criarPropostaInicial(payload) {
  return apiRequest('/servicos/criar', { method: 'POST', body: payload })
}

export function aceitarProposta(id, payload) {
  return apiRequest(`/servicos/${id}/aceitar`, { method: 'POST', body: payload })
}

export function negarProposta(id) {
  return apiRequest(`/servicos/${id}/negar`, { method: 'POST' })
}

export function confirmarServico(id) {
  return apiRequest(`/servicos/${id}/confirmar`, { method: 'POST' })
}

export function recusarServico(id) {
  return apiRequest(`/servicos/${id}/recusar`, { method: 'POST' })
}

export function proporData(id, dataHorario) {
  return apiRequest(`/servicos/${id}/propor-data`, { method: 'POST', body: { dataHorario } })
}

