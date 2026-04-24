import { apiRequest } from './api'

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

