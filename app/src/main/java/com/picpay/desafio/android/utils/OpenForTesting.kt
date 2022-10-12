package com.picpay.desafio.android.utils

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class OpenClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class OpenForTesting
