import React from 'react';
import { Button, Dialog, DialogTitle, DialogContent, DialogActions }from '@mui/material';

export default function CustomizedAlerts(props: any) {
  return (
    <div>
      <Dialog onClose={props.onClose} aria-labelledby="customized-dialog-title" maxWidth={false} open={props.open}>
         <DialogTitle id="customized-dialog-title" style={props.style}>
            {props.title}
         </DialogTitle>
         <DialogContent dividers>
            {props.children}
         </DialogContent>
         <DialogActions>
            <Button autoFocus onClick={props.onClose} color="primary">
               {props.closeText}
            </Button>
            <Button autoFocus onClick={props.onSave} color="primary">
               {props.saveText}
            </Button>
         </DialogActions>
      </Dialog>
    </div>
  );
}