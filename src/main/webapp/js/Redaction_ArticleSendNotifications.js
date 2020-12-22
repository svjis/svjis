"use strict";

function checkAll() {
    let control = document.getElementById('check-all');
    let check = (control.checked) ? false : true;
    let table = document.getElementById('recipient-list');
    
    let row = undefined;
    let i = 1;
    do {
        row = table.getElementsByTagName('tr')[i];
        if (row !== undefined) {
            let col = row.getElementsByTagName('td')[0];
            if (col !== undefined) {
                col.getElementsByTagName('input')[0].checked = !check;
            }
        }
        i++;
    } while(row !== undefined);
}
