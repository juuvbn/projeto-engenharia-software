import { apiRequest } from './api'

export function listarServicosDoPrestador({ busca, status, page = 0, size = 20 } = {}) {
  const params = new URLSearchParams()
  if (busca) params.append('busca', busca)
  if (status?.length) status.forEach(s => params.append('status', s))
  params.append('page', String(page))
  params.append('size', String(size))
  return apiRequest(`/prestadores/me/servicos?${params.toString()}`, { method: 'GET' })
}

export async function listarPrestadores({ especialidades = [], page = 0, size = 12 } = {}) {
  const params = new URLSearchParams()

  if (page !== undefined && page !== null) {
    params.append('page', String(page))
  }

  if (size !== undefined && size !== null) {
    params.append('size', String(size))
  }

  for (const especialidade of especialidades) {
    params.append('especialidades', especialidade)
  }

  const query = params.toString()
  const path = query ? `/prestadores?${query}` : '/prestadores'

  return apiRequest(path, { method: 'GET' })
}

