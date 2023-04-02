export function formatAll(data: any)
{
   console.log(data)
   let relays: any = [];

   relays = data.relays.map((relay: any) => {
      relay.environment = data.environments.filter((environment: any) => (relay.environmentId === environment.id))[0];
      relay.board = data.boards.filter((board: any) => relay.boardId === board.id)[0];
      relay.type = data.relayTypes.filter((relayType: any) => relay.typeId === relayType.id)[0];
      
      return relay;
   });

   console.log(relays)
   return relays;
}

export function formatOne(data: any)
{
   console.log(data)
   let relay: any = data.relay;

   relay.environment = data.environment;
   relay.board = data.board;
   relay.type = data.relayType;

   console.log(relay)
   return relay;
}