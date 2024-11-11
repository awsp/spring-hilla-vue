package com.example.hilla;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {

  private String id;
  private String title;
  private String firstName;
  private String lastName;
  private boolean marketing;
}
