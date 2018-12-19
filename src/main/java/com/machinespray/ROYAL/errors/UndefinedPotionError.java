package com.machinespray.ROYAL.errors;

public class UndefinedPotionError extends Error {
	public UndefinedPotionError(String attempt) {
		super("Attempt to get potion with id" + attempt);
	}
}
