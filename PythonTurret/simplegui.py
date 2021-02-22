import PySimpleGUI as sg
import sys

def createWindow():
    sg.change_look_and_feel('Dark Blue 3');

    # Create COM port default for windows or linux
    default_serial_port = 'COM0';
    if (sys.platform == 'linux'):
        default_serial_port = '/dev/ttyUSB0';

    layout = [
        [sg.Text('Capture device index:', size=(20,1)), sg.Input('0', justification='center', size=(20,1))],
        [sg.Text('COM port', size=(20,1)), sg.InputText(default_serial_port, justification='center', size=(20,1))],
        [sg.Text(' ' * 30), sg.Button('Run')]
    ];

    window = sg.Window('Turret', layout=layout, default_element_size=(3,1));

    while True:
        # read the window
        event, values = window.read();

        # exit on Quit
        if event in ('Quit', None):
            window.close();
            return None;

        # button callback function
        if (event == 'Run'):
            return {'capture_index': values[0], 'COM': values[1]};
            window.close();