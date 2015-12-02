package com.qualityeclipse.sample;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class PersonListLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		Person person = (Person) element;
		return person.firstName + " " + person.lastName;
	}
}
