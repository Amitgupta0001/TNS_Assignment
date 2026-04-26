const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

function buildUrl(path, query = {}) {
  const params = new URLSearchParams()
  Object.entries(query).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      params.append(key, String(value))
    }
  })

  const queryString = params.toString()
  return `${API_BASE_URL}${path}${queryString ? `?${queryString}` : ''}`
}

async function request(path, options = {}, expectText = false) {
  const response = await fetch(buildUrl(path, options.query), {
    method: options.method || 'GET',
    headers: {
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...(options.headers || {}),
    },
    body: options.body,
  })

  if (!response.ok) {
    let errorMessage = `Request failed with status ${response.status}`
    try {
      const errorJson = await response.json()
      errorMessage = errorJson.message || errorJson.error || errorMessage
    } catch {
      const errorText = await response.text()
      if (errorText) {
        errorMessage = errorText
      }
    }
    throw new Error(errorMessage)
  }

  if (expectText) {
    return response.text()
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

export function registerUser(payload) {
  return request('/auth/register', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export function loginUser(payload) {
  return request(
    '/auth/login',
    {
      method: 'POST',
      body: JSON.stringify(payload),
    },
    true,
  )
}

export function verifyEmail(token) {
  return request('/auth/verify', {
    query: { token },
  }, true)
}

export function getDashboardStats(role) {
  return request('/dashboard/stats', {
    query: { role },
  })
}

export function getAllStudents(role) {
  return request('/students', {
    query: { role },
  })
}

export function addStudent(payload, role) {
  return request('/students', {
    method: 'POST',
    body: JSON.stringify(payload),
    query: { role },
  })
}

export function updateStudent(id, payload, role) {
  return request(`/students/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
    query: { role },
  })
}

export function deleteStudent(id, role) {
  return request(`/students/${id}`, {
    method: 'DELETE',
    query: { role },
  }, true)
}

export function searchStudentByHallTicket(hallTicket, role) {
  return request(`/students/hall/${hallTicket}`, {
    query: { role },
  })
}

export function searchStudentsByName(name, role) {
  return request(`/students/name/${encodeURIComponent(name)}`, {
    query: { role },
  })
}

export function getAllColleges(role) {
  return request('/colleges', {
    query: { role },
  })
}

export function addCollege(payload, role) {
  return request('/colleges', {
    method: 'POST',
    body: JSON.stringify(payload),
    query: { role },
  })
}

export function updateCollege(id, payload, role) {
  return request(`/colleges/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
    query: { role },
  })
}

export function deleteCollege(id, role) {
  return request(`/colleges/${id}`, {
    method: 'DELETE',
    query: { role },
  }, true)
}

export function getAllPlacements(role) {
  return request('/placements', {
    query: { role },
  })
}

export function getPlacementsByYear(year, role) {
  return request(`/placements/year/${year}`, {
    query: { role },
  })
}

export function addPlacement(payload, role) {
  return request('/placements', {
    method: 'POST',
    body: JSON.stringify(payload),
    query: { role },
  })
}

export function updatePlacement(id, payload, role) {
  return request(`/placements/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
    query: { role },
  })
}

export function deletePlacement(id, role) {
  return request(`/placements/${id}`, {
    method: 'DELETE',
    query: { role },
  }, true)
}

export function getAllCertificates(role) {
  return request('/certificates', {
    query: { role },
  })
}

export function addCertificate(payload, role) {
  return request('/certificates', {
    method: 'POST',
    body: JSON.stringify(payload),
    query: { role },
  })
}

export function updateCertificate(id, payload, role) {
  return request(`/certificates/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
    query: { role },
  })
}

export function deleteCertificate(id, role) {
  return request(`/certificates/${id}`, {
    method: 'DELETE',
    query: { role },
  }, true)
}
