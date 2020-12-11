"use strict";

let noDelete = getLastOptionNo();
let desc = document.getElementById('opt_parent').getElementsByTagName('th')[0].innerHTML;


function getLastOptionNo() {
    let i = 0;
    let p;

    do {
        i++;
        p = document.getElementById('opt_' + i);
    } while (p !== null);

    return i - 1;
}

function addOption() {
    let lastOptionNo = getLastOptionNo();
    let elem = document.getElementById('opt_' + lastOptionNo);
    
    let clone = elem.cloneNode(true);
    lastOptionNo ++;
    clone.id = 'opt_' + lastOptionNo;
    clone.getElementsByTagName('th')[0].innerHTML = desc.replace('1', lastOptionNo);
    clone.getElementsByTagName('td')[0].getElementsByTagName('input')[0].name = 'oid_' + lastOptionNo;
    clone.getElementsByTagName('td')[0].getElementsByTagName('input')[0].value = '0';
    clone.getElementsByTagName('td')[0].getElementsByTagName('input')[1].id = 'o' + lastOptionNo + '-input';
    clone.getElementsByTagName('td')[0].getElementsByTagName('input')[1].name = 'o_' + lastOptionNo;
    clone.getElementsByTagName('td')[0].getElementsByTagName('input')[1].value = '';
    clone.getElementsByTagName('td')[1].innerHTML = '&nbsp;';
    
    elem.after(clone);
    
    let button = document.getElementById('remove-option');
    button.disabled = false;
}

function removeOption() {
    let lastOptionNo = getLastOptionNo();

    if (lastOptionNo !== noDelete) {
        let element = document.getElementById('opt_' + lastOptionNo);
        element.parentNode.removeChild(element);
    }
    if (noDelete === (lastOptionNo - 1)) {
        let button = document.getElementById('remove-option');
        button.disabled = true;
    }
}
