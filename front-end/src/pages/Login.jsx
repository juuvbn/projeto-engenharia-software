function Login() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md w-80">
        
        <h2 className="text-2xl font-bold mb-6 text-center">
          Login
        </h2>

        <form className="flex flex-col gap-4">
          
          <input
            type="email"
            placeholder="Email"
            className="border p-2 rounded"
          />

          <input
            type="password"
            placeholder="Senha"
            className="border p-2 rounded"
          />

          <button className="bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
            Entrar
          </button>

        </form>

        <p className="text-sm text-center mt-4">
          Não tem conta?{" "}
          <a href="/cadastro" className="text-blue-600">
            Cadastre-se
          </a>
        </p>

      </div>
    </div>
  )
}

export default Login