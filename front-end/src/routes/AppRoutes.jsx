import { Routes, Route } from 'react-router-dom'
import Home from '../pages/Home'
import SobreNos from '../pages/SobreNos'
import Login from '../pages/Login'
import Cadastro from '../pages/Cadastro'
import RecuperarSenha from '../pages/RecuperarSenha'
import BuscarServico from '../pages/BuscarServico'
import SelecionarProfissional from '../pages/SelecionarProfissional'
import ConfirmacaoSelecao from '../pages/ConfirmacaoSelecao'
import SemResultado from '../pages/SemResultado'
import CadastroCliente from '../pages/CadastroCliente'
import CadastroPrestador from '../pages/CadastroPrestador'
import MeusServicos from '../pages/MeusServicos'
import Perfil from '../pages/Perfil'
import EditarPerfil from '../pages/EditarPerfil'
import AlterarSenha from '../pages/AlterarSenha'

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/sobre" element={<SobreNos />} />
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />
      <Route path="/recuperar-senha" element={<RecuperarSenha />} />
      <Route path="/buscar-servico" element={<BuscarServico />} />
      <Route path="/selecionar-profissional" element={<SelecionarProfissional />} />
      <Route path="/confirmacao" element={<ConfirmacaoSelecao />} />
      <Route path="/sem-resultado" element={<SemResultado />} />
      <Route path="/cadastro-cliente" element={<CadastroCliente />} />
      <Route path="/cadastro-prestador" element={<CadastroPrestador />} />
      <Route path="/meus-servicos" element={<MeusServicos />} />
      <Route path="/perfil" element={<Perfil />} />
      <Route path="/perfil/editar" element={<EditarPerfil />} />
      <Route path="/perfil/senha" element={<AlterarSenha />} />
    </Routes>
  )
}

export default AppRoutes
