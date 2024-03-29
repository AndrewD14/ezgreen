import Grid from '@mui/material/Unstable_Grid2';
import logo from '../../../logo.svg';

function Header() {
   return (
      <Grid container spacing={2} minHeight={160}>
         <Grid xs display='flex' justifyContent="center" alignItems="center">
            <img src={logo} alt="logo" style={{height: '8em' }}/>
         </Grid>
         <Grid display='flex' justifyContent="right" alignItems="end">
            {process.env.REACT_APP_USERNAME}
         </Grid>
      </Grid>
   );
}

export default Header;