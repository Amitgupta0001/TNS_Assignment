import { useEffect, useMemo, useState } from 'react'
import {
  addCertificate,
  addCollege,
  addPlacement,
  addStudent,
  deleteCertificate,
  deleteCollege,
  deletePlacement,
  deleteStudent,
  getAllCertificates,
  getAllColleges,
  getDashboardStats,
  getAllPlacements,
  getAllStudents,
  getPlacementsByYear,
  loginUser,
  registerUser,
  searchStudentByHallTicket,
  searchStudentsByName,
  updateCertificate,
  updateCollege,
  updatePlacement,
  updateStudent,
  verifyEmail,
} from './api'
import './App.css'

function App() {
  const [students, setStudents] = useState([])
  const [colleges, setColleges] = useState([])
  const [placements, setPlacements] = useState([])
  const [certificates, setCertificates] = useState([])
  const [stats, setStats] = useState({})
  const [activeTab, setActiveTab] = useState('students')
  const [status, setStatus] = useState('')
  const [loading, setLoading] = useState(false)
  const [apiRole, setApiRole] = useState('ADMIN')

  const [registerForm, setRegisterForm] = useState({
    name: '',
    email: '',
    password: '',
    type: 'USER',
  })
  const [loginForm, setLoginForm] = useState({
    name: '',
    password: '',
  })
  const [verifyToken, setVerifyToken] = useState('')

  const [studentForm, setStudentForm] = useState({
    name: '',
    qualification: '',
    course: '',
    year: '',
    hallTicketNo: '',
  })
  const [studentSearch, setStudentSearch] = useState({
    hallTicket: '',
    name: '',
  })
  const [studentSearchResults, setStudentSearchResults] = useState([])
  const [editingStudentId, setEditingStudentId] = useState(null)

  const [collegeForm, setCollegeForm] = useState({
    collegeName: '',
    location: '',
  })
  const [editingCollegeId, setEditingCollegeId] = useState(null)

  const [placementForm, setPlacementForm] = useState({
    name: '',
    qualification: '',
    year: '',
    companyName: '',
    packageOffered: '',
    location: '',
  })
  const [editingPlacementId, setEditingPlacementId] = useState(null)
  const [placementFilterYear, setPlacementFilterYear] = useState('')
  const [placementFilterResults, setPlacementFilterResults] = useState([])

  const [certificateForm, setCertificateForm] = useState({
    year: '',
    studentId: '',
    fileName: '',
  })
  const [editingCertificateId, setEditingCertificateId] = useState(null)

  const tabs = useMemo(
    () => [
      { id: 'students', label: 'Students' },
      { id: 'colleges', label: 'Colleges' },
      { id: 'placements', label: 'Placements' },
      { id: 'certificates', label: 'Certificates' },
      { id: 'auth', label: 'Auth' },
    ],
    [],
  )

  const runAction = async (action, successText) => {
    setLoading(true)
    setStatus('')
    try {
      await action()
      setStatus(successText)
    } catch (error) {
      setStatus(error.message || 'Something went wrong')
    } finally {
      setLoading(false)
    }
  }

  const loadStudents = async () => {
    const data = await getAllStudents(apiRole)
    setStudents(data)
  }

  const loadColleges = async () => {
    const data = await getAllColleges(apiRole)
    setColleges(data)
  }

  const loadPlacements = async () => {
    const data = await getAllPlacements(apiRole)
    setPlacements(data)
  }

  const loadCertificates = async () => {
    const data = await getAllCertificates(apiRole)
    setCertificates(data)
  }

  const loadStats = async () => {
    const data = await getDashboardStats(apiRole)
    setStats(data)
  }

  const loadDashboard = async () => {
    try {
      await Promise.all([
        loadStudents(),
        loadColleges(),
        loadPlacements(),
        loadCertificates(),
        loadStats(),
      ])
    } catch (error) {
      setStatus(error.message || 'Unable to load dashboard data')
    }
  }

  useEffect(() => {
    const timerId = setTimeout(() => {
      void (async () => {
        setLoading(true)
        setStatus('Loading data...')
        try {
          const [studentsData, collegesData, placementsData, certificatesData, statsData] =
            await Promise.all([
              getAllStudents(apiRole),
              getAllColleges(apiRole),
              getAllPlacements(apiRole),
              getAllCertificates(apiRole),
              getDashboardStats(apiRole),
            ])

          setStudents(studentsData)
          setColleges(collegesData)
          setPlacements(placementsData)
          setCertificates(certificatesData)
          setStats(statsData)
          setStatus('Data loaded')
        } catch (error) {
          setStatus(error.message || 'Unable to load dashboard data')
        } finally {
          setLoading(false)
        }
      })()
    }, 0)

    return () => clearTimeout(timerId)
  }, [apiRole])

  const handleRefresh = async () => {
    await runAction(async () => {
      await loadDashboard()
    }, 'Data loaded')
  }

  const handleRegister = async (event) => {
    event.preventDefault()
    await runAction(async () => {
      await registerUser(registerForm)
      setRegisterForm({ name: '', email: '', password: '', type: 'USER' })
    }, 'User registered successfully')
  }

  const handleLogin = async (event) => {
    event.preventDefault()
    setLoading(true)
    setStatus('')
    try {
      const message = await loginUser(loginForm)
      setStatus(`Login response: ${message}`)
    } catch (error) {
      setStatus(error.message || 'Login failed')
    } finally {
      setLoading(false)
    }
  }

  const handleStudentAdd = async (event) => {
    event.preventDefault()
    await runAction(async () => {
      const payload = {
        ...studentForm,
        year: Number(studentForm.year),
        hallTicketNo: Number(studentForm.hallTicketNo),
      }

      if (editingStudentId) {
        await updateStudent(editingStudentId, payload, apiRole)
      } else {
        await addStudent(payload, apiRole)
      }

      setStudentForm({
        name: '',
        qualification: '',
        course: '',
        year: '',
        hallTicketNo: '',
      })
      setEditingStudentId(null)
      await loadStudents()
    }, editingStudentId ? 'Student updated successfully' : 'Student added successfully')
  }

  const handleStudentDelete = async (id) => {
    await runAction(async () => {
      await deleteStudent(id, apiRole)
      await loadStudents()
    }, 'Student deleted successfully')
  }

  const startEditStudent = (student) => {
    setActiveTab('students')
    setEditingStudentId(student.id)
    setStudentForm({
      name: student.name || '',
      qualification: student.qualification || '',
      course: student.course || '',
      year: String(student.year ?? ''),
      hallTicketNo: String(student.hallTicketNo ?? ''),
    })
  }

  const cancelEditStudent = () => {
    setEditingStudentId(null)
    setStudentForm({
      name: '',
      qualification: '',
      course: '',
      year: '',
      hallTicketNo: '',
    })
  }

  const handleStudentSearch = async () => {
    await runAction(async () => {
      if (studentSearch.hallTicket) {
        const result = await searchStudentByHallTicket(
          Number(studentSearch.hallTicket),
          apiRole,
        )
        setStudentSearchResults(result ? [result] : [])
        return
      }
      if (studentSearch.name.trim()) {
        const result = await searchStudentsByName(studentSearch.name.trim(), apiRole)
        setStudentSearchResults(result)
        return
      }
      setStudentSearchResults([])
    }, 'Student search complete')
  }

  const handleCollegeAdd = async (event) => {
    event.preventDefault()
    await runAction(async () => {
      if (editingCollegeId) {
        await updateCollege(editingCollegeId, collegeForm, apiRole)
      } else {
        await addCollege(collegeForm, apiRole)
      }
      setCollegeForm({ collegeName: '', location: '' })
      setEditingCollegeId(null)
      await loadColleges()
    }, editingCollegeId ? 'College updated successfully' : 'College added successfully')
  }

  const handleCollegeDelete = async (id) => {
    await runAction(async () => {
      await deleteCollege(id, apiRole)
      await loadColleges()
    }, 'College deleted successfully')
  }

  const startEditCollege = (college) => {
    setActiveTab('colleges')
    setEditingCollegeId(college.id)
    setCollegeForm({
      collegeName: college.collegeName || '',
      location: college.location || '',
    })
  }

  const cancelEditCollege = () => {
    setEditingCollegeId(null)
    setCollegeForm({
      collegeName: '',
      location: '',
    })
  }

  const handlePlacementAdd = async (event) => {
    event.preventDefault()
    await runAction(async () => {
      const payload = {
        ...placementForm,
        year: Number(placementForm.year),
      }

      if (editingPlacementId) {
        await updatePlacement(editingPlacementId, payload, apiRole)
      } else {
        await addPlacement(payload, apiRole)
      }

      setPlacementForm({
        name: '',
        qualification: '',
        year: '',
        companyName: '',
        packageOffered: '',
        location: '',
      })
      setEditingPlacementId(null)
      await loadPlacements()
    }, editingPlacementId ? 'Placement updated successfully' : 'Placement added successfully')
  }

  const handlePlacementDelete = async (id) => {
    await runAction(async () => {
      await deletePlacement(id, apiRole)
      await loadPlacements()
    }, 'Placement deleted successfully')
  }

  const startEditPlacement = (placement) => {
    setActiveTab('placements')
    setEditingPlacementId(placement.id)
    setPlacementForm({
      name: placement.name || '',
      qualification: placement.qualification || '',
      year: String(placement.year ?? ''),
      companyName: placement.companyName || '',
      packageOffered: placement.packageOffered || '',
      location: placement.location || '',
    })
  }

  const cancelEditPlacement = () => {
    setEditingPlacementId(null)
    setPlacementForm({
      name: '',
      qualification: '',
      year: '',
      companyName: '',
      packageOffered: '',
      location: '',
    })
  }

  const handlePlacementFilter = async () => {
    await runAction(async () => {
      if (!placementFilterYear) {
        setPlacementFilterResults([])
        return
      }
      const result = await getPlacementsByYear(Number(placementFilterYear), apiRole)
      setPlacementFilterResults(result)
    }, 'Placement filter complete')
  }

  const handleCertificateAdd = async (event) => {
    event.preventDefault()
    await runAction(async () => {
      const payload = {
        year: Number(certificateForm.year),
        fileName: certificateForm.fileName,
        student: { id: Number(certificateForm.studentId) },
      }

      if (editingCertificateId) {
        await updateCertificate(editingCertificateId, payload, apiRole)
      } else {
        await addCertificate(payload, apiRole)
      }

      setCertificateForm({ year: '', studentId: '', fileName: '' })
      setEditingCertificateId(null)
      await loadCertificates()
    }, editingCertificateId ? 'Certificate updated successfully' : 'Certificate added successfully')
  }

  const handleCertificateDelete = async (id) => {
    await runAction(async () => {
      await deleteCertificate(id, apiRole)
      await loadCertificates()
    }, 'Certificate deleted successfully')
  }

  const startEditCertificate = (certificate) => {
    setActiveTab('certificates')
    setEditingCertificateId(certificate.id)
    setCertificateForm({
      year: String(certificate.year ?? ''),
      studentId: String(certificate.student?.id ?? ''),
      fileName: certificate.fileName || '',
    })
  }

  const cancelEditCertificate = () => {
    setEditingCertificateId(null)
    setCertificateForm({ year: '', studentId: '', fileName: '' })
  }

  const handleVerifyEmail = async (event) => {
    event.preventDefault()
    await runAction(async () => {
      const message = await verifyEmail(verifyToken.trim())
      setVerifyToken('')
      setStatus(message || 'Email verified successfully')
    }, 'Verification complete')
  }

  return (
    <main className="page">
      <header className="hero">
        <p className="eyebrow">TNS Assignment</p>
        <h1>Placement Control Room</h1>
        <p className="subtitle">
          React frontend connected to your Spring Boot backend for student,
          college, placement, certificate, and auth workflows.
        </p>
        <button className="refresh" onClick={handleRefresh} disabled={loading}>
          {loading ? 'Working...' : 'Refresh Data'}
        </button>
      </header>

      <section className="tabs">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            className={activeTab === tab.id ? 'tab active' : 'tab'}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label}
          </button>
        ))}
      </section>

      <section className="panel stats-grid">
        <article className="card">
          <p>Students</p>
          <h3>{stats.students ?? '-'}</h3>
        </article>
        <article className="card">
          <p>Colleges</p>
          <h3>{stats.colleges ?? '-'}</h3>
        </article>
        <article className="card">
          <p>Placements</p>
          <h3>{stats.placements ?? '-'}</h3>
        </article>
        <article className="card">
          <p>Certificates</p>
          <h3>{stats.certificates ?? '-'}</h3>
        </article>
        <article className="card">
          <p>Users</p>
          <h3>{stats.users ?? '-'}</h3>
        </article>
      </section>

      <section className="status">{status || 'Ready'}</section>

      {activeTab === 'students' && (
        <section className="panel">
          <h2>Students</h2>
          {editingStudentId && <p className="edit-banner">Editing student #{editingStudentId}</p>}
          <form className="form-grid" onSubmit={handleStudentAdd}>
            <input
              placeholder="Name"
              value={studentForm.name}
              onChange={(event) =>
                setStudentForm({ ...studentForm, name: event.target.value })
              }
              required
            />
            <input
              placeholder="Qualification"
              value={studentForm.qualification}
              onChange={(event) =>
                setStudentForm({
                  ...studentForm,
                  qualification: event.target.value,
                })
              }
              required
            />
            <input
              placeholder="Course"
              value={studentForm.course}
              onChange={(event) =>
                setStudentForm({ ...studentForm, course: event.target.value })
              }
              required
            />
            <input
              type="number"
              placeholder="Year"
              value={studentForm.year}
              onChange={(event) =>
                setStudentForm({ ...studentForm, year: event.target.value })
              }
              required
            />
            <input
              type="number"
              placeholder="Hall Ticket No"
              value={studentForm.hallTicketNo}
              onChange={(event) =>
                setStudentForm({
                  ...studentForm,
                  hallTicketNo: event.target.value,
                })
              }
              required
            />
            <button type="submit" disabled={loading}>
              {editingStudentId ? 'Update Student' : 'Add Student'}
            </button>
            {editingStudentId && (
              <button type="button" className="secondary" onClick={cancelEditStudent}>
                Cancel
              </button>
            )}
          </form>

          <div className="search-row">
            <input
              type="number"
              placeholder="Search by hall ticket"
              value={studentSearch.hallTicket}
              onChange={(event) =>
                setStudentSearch({
                  ...studentSearch,
                  hallTicket: event.target.value,
                })
              }
            />
            <input
              placeholder="Search by exact name"
              value={studentSearch.name}
              onChange={(event) =>
                setStudentSearch({ ...studentSearch, name: event.target.value })
              }
            />
            <button onClick={handleStudentSearch} disabled={loading}>
              Search
            </button>
          </div>

          {studentSearchResults.length > 0 && (
            <div className="result-box">
              <h3>Search Results</h3>
              {studentSearchResults.map((student) => (
                <p key={`search-${student.id}`}>
                  {student.id} | {student.name} | {student.qualification} |
                  {' '}Hall: {student.hallTicketNo}
                </p>
              ))}
            </div>
          )}

          <div className="list">
            {students.map((student) => (
              <article key={student.id} className="card">
                <p>{student.name}</p>
                <small>
                  {student.qualification} | {student.course} | Year {student.year}
                </small>
                <small>Hall Ticket: {student.hallTicketNo}</small>
                <div className="card-actions">
                  <button type="button" className="secondary" onClick={() => startEditStudent(student)}>
                    Edit
                  </button>
                  <button type="button" onClick={() => handleStudentDelete(student.id)}>
                    Delete
                  </button>
                </div>
              </article>
            ))}
          </div>
        </section>
      )}

      {activeTab === 'colleges' && (
        <section className="panel">
          <h2>Colleges</h2>
          {editingCollegeId && <p className="edit-banner">Editing college #{editingCollegeId}</p>}
          <form className="form-grid" onSubmit={handleCollegeAdd}>
            <input
              placeholder="College Name"
              value={collegeForm.collegeName}
              onChange={(event) =>
                setCollegeForm({ ...collegeForm, collegeName: event.target.value })
              }
              required
            />
            <input
              placeholder="Location"
              value={collegeForm.location}
              onChange={(event) =>
                setCollegeForm({ ...collegeForm, location: event.target.value })
              }
              required
            />
            <button type="submit" disabled={loading}>
              {editingCollegeId ? 'Update College' : 'Add College'}
            </button>
            {editingCollegeId && (
              <button type="button" className="secondary" onClick={cancelEditCollege}>
                Cancel
              </button>
            )}
          </form>
          <div className="list">
            {colleges.map((college) => (
              <article key={college.id} className="card">
                <p>{college.collegeName}</p>
                <small>{college.location}</small>
                <div className="card-actions">
                  <button type="button" className="secondary" onClick={() => startEditCollege(college)}>
                    Edit
                  </button>
                  <button type="button" onClick={() => handleCollegeDelete(college.id)}>
                    Delete
                  </button>
                </div>
              </article>
            ))}
          </div>
        </section>
      )}

      {activeTab === 'placements' && (
        <section className="panel">
          <h2>Placements</h2>
          {editingPlacementId && <p className="edit-banner">Editing placement #{editingPlacementId}</p>}
          <form className="form-grid" onSubmit={handlePlacementAdd}>
            <input
              placeholder="Name"
              value={placementForm.name}
              onChange={(event) =>
                setPlacementForm({ ...placementForm, name: event.target.value })
              }
              required
            />
            <input
              placeholder="Qualification"
              value={placementForm.qualification}
              onChange={(event) =>
                setPlacementForm({
                  ...placementForm,
                  qualification: event.target.value,
                })
              }
              required
            />
            <input
              type="number"
              placeholder="Year"
              value={placementForm.year}
              onChange={(event) =>
                setPlacementForm({ ...placementForm, year: event.target.value })
              }
              required
            />
            <input
              placeholder="Company Name"
              value={placementForm.companyName}
              onChange={(event) =>
                setPlacementForm({
                  ...placementForm,
                  companyName: event.target.value,
                })
              }
              required
            />
            <input
              placeholder="Package Offered"
              value={placementForm.packageOffered}
              onChange={(event) =>
                setPlacementForm({
                  ...placementForm,
                  packageOffered: event.target.value,
                })
              }
              required
            />
            <input
              placeholder="Location"
              value={placementForm.location}
              onChange={(event) =>
                setPlacementForm({
                  ...placementForm,
                  location: event.target.value,
                })
              }
              required
            />
            <button type="submit" disabled={loading}>
              {editingPlacementId ? 'Update Placement' : 'Add Placement'}
            </button>
            {editingPlacementId && (
              <button type="button" className="secondary" onClick={cancelEditPlacement}>
                Cancel
              </button>
            )}
          </form>

          <div className="search-row">
            <input
              type="number"
              placeholder="Filter by year"
              value={placementFilterYear}
              onChange={(event) => setPlacementFilterYear(event.target.value)}
            />
            <button onClick={handlePlacementFilter} disabled={loading}>
              Apply Filter
            </button>
          </div>

          {placementFilterResults.length > 0 && (
            <div className="result-box">
              <h3>Year Filter Results</h3>
              {placementFilterResults.map((placement) => (
                <p key={`filter-${placement.id}`}>
                  {placement.id} | {placement.name} | {placement.qualification} |
                  {' '}Year {placement.year}
                </p>
              ))}
            </div>
          )}

          <div className="list">
            {placements.map((placement) => (
              <article key={placement.id} className="card">
                <p>{placement.name}</p>
                <small>
                  {placement.qualification} | Year {placement.year}
                </small>
                <small>
                  {placement.companyName} | {placement.packageOffered}
                </small>
                <small>{placement.location}</small>
                <div className="card-actions">
                  <button type="button" className="secondary" onClick={() => startEditPlacement(placement)}>
                    Edit
                  </button>
                  <button type="button" onClick={() => handlePlacementDelete(placement.id)}>
                    Delete
                  </button>
                </div>
              </article>
            ))}
          </div>
        </section>
      )}

      {activeTab === 'certificates' && (
        <section className="panel">
          <h2>Certificates</h2>
          {editingCertificateId && <p className="edit-banner">Editing certificate #{editingCertificateId}</p>}
          <form className="form-grid" onSubmit={handleCertificateAdd}>
            <input
              type="number"
              placeholder="Year"
              value={certificateForm.year}
              onChange={(event) =>
                setCertificateForm({ ...certificateForm, year: event.target.value })
              }
              required
            />
            <input
              type="number"
              placeholder="Student ID"
              value={certificateForm.studentId}
              onChange={(event) =>
                setCertificateForm({
                  ...certificateForm,
                  studentId: event.target.value,
                })
              }
              required
            />
            <input
              placeholder="Certificate file name"
              value={certificateForm.fileName}
              onChange={(event) =>
                setCertificateForm({
                  ...certificateForm,
                  fileName: event.target.value,
                })
              }
              required
            />
            <button type="submit" disabled={loading}>
              {editingCertificateId ? 'Update Certificate' : 'Add Certificate'}
            </button>
            {editingCertificateId && (
              <button type="button" className="secondary" onClick={cancelEditCertificate}>
                Cancel
              </button>
            )}
          </form>

          <div className="list">
            {certificates.map((certificate) => (
              <article key={certificate.id} className="card">
                <p>Year {certificate.year}</p>
                <small>
                  Student ID: {certificate.student?.id ?? 'N/A'} | File:
                  {' '}
                  {certificate.fileName}
                </small>
                <div className="card-actions">
                  <button type="button" className="secondary" onClick={() => startEditCertificate(certificate)}>
                    Edit
                  </button>
                  <button type="button" onClick={() => handleCertificateDelete(certificate.id)}>
                    Delete
                  </button>
                </div>
              </article>
            ))}
          </div>
        </section>
      )}

      {activeTab === 'auth' && (
        <section className="panel auth-grid">
          <div>
            <h2>Register</h2>
            <form className="form-grid" onSubmit={handleRegister}>
              <input
                placeholder="Name"
                value={registerForm.name}
                onChange={(event) =>
                  setRegisterForm({ ...registerForm, name: event.target.value })
                }
                required
              />
              <input
                placeholder="Email"
                type="email"
                value={registerForm.email}
                onChange={(event) =>
                  setRegisterForm({ ...registerForm, email: event.target.value })
                }
                required
              />
              <input
                placeholder="Password"
                type="password"
                value={registerForm.password}
                onChange={(event) =>
                  setRegisterForm({
                    ...registerForm,
                    password: event.target.value,
                  })
                }
                required
              />
              <select
                value={registerForm.type}
                onChange={(event) =>
                  setRegisterForm({ ...registerForm, type: event.target.value })
                }
              >
                <option value="USER">USER</option>
              </select>
              <button type="submit" disabled={loading}>
                Register
              </button>
            </form>
          </div>
          <div>
            <h2>Login</h2>
            <form className="form-grid" onSubmit={handleLogin}>
              <input
                placeholder="Name"
                value={loginForm.name}
                onChange={(event) =>
                  setLoginForm({ ...loginForm, name: event.target.value })
                }
                required
              />
              <input
                placeholder="Password"
                type="password"
                value={loginForm.password}
                onChange={(event) =>
                  setLoginForm({ ...loginForm, password: event.target.value })
                }
                required
              />
              <button type="submit" disabled={loading}>
                Login
              </button>
            </form>
            <form className="form-grid" onSubmit={handleVerifyEmail}>
              <input
                placeholder="Verification token"
                value={verifyToken}
                onChange={(event) => setVerifyToken(event.target.value)}
                required
              />
              <button type="submit" disabled={loading}>
                Verify Email
              </button>
            </form>
          </div>
          <div>
            <h2>Access Role</h2>
            <p>
              All management APIs require ADMIN role in backend.
            </p>
            <form className="form-grid" onSubmit={(event) => event.preventDefault()}>
              <select
                value={apiRole}
                onChange={(event) => setApiRole(event.target.value)}
              >
                <option value="ADMIN">ADMIN</option>
                <option value="USER">USER</option>
              </select>
            </form>
          </div>
        </section>
      )}
    </main>
  )
}

export default App
