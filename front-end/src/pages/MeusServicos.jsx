import React from 'react';
import MeusServicosCliente from './MeusServicosCliente';
import MeusServicosPrestador from './MeusServicosPrestador';

const MeusServicos = () => {
  // Simulação: aguardando endpoint do backend
  const userRole = 'prestador'; 

  if (userRole === 'prestador') {
    return <MeusServicosPrestador />;
  }

  return <MeusServicosCliente />;
};

export default MeusServicos;
