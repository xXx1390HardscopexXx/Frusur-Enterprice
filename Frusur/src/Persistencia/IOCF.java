package Persistencia;

import Excepciones.CFException;

import java.io.*;

public class IOCF {
    private static IOCF instance;

    private IOCF() {}

    public static IOCF getInstance() {
        if (instance == null) {
            instance = new IOCF();
        }
        return instance;
    }

    public void guardar(Object[] controladores) throws CFException {
        try{
            ObjectOutputStream archivo = new ObjectOutputStream(new FileOutputStream("CFObjetos.obj"));
            archivo.writeObject(controladores);
            archivo.close();
        }catch(FileNotFoundException e){
            throw new CFException("No se puede abrir o crear el archivo CFObjetos.obj");
        }catch(IOException e){
            throw new CFException("No se puede grabar en el archivo CFObjetos.obj");
        }
    }

    public Object[] cargar() throws CFException {
        ObjectInputStream archivo = null;
        try {
            archivo = new ObjectInputStream(new FileInputStream("CFObjetos.obj"));
            Object[] controlador = (Object[]) archivo.readObject();
            archivo.close();
            return controlador;
        } catch (EOFException ex) {
            throw new CFException("Error");
        } catch (FileNotFoundException ex) {
            throw new CFException("No existe o no se puede abrir el archivo CFObjetos.obj");
        } catch (IOException | ClassNotFoundException ex) {
            throw new CFException("No se puede leer el archivo CFObjetos.obj");
        }
    }
}
