package com.qualityeclipse.sample;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class PersonTableLabelProvider 
	extends LabelProvider 
	implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int index) {
		Person person = (Person) element;
		switch (index) {
			case 0 :
				return person.firstName;
			case 1 :
				return person.lastName;
			case 2 :
				return Integer.toString(person.age);
			case 3 :
				return Integer.toString(person.children.length);
			default :
				break;
		}
		return "";
	}
}
